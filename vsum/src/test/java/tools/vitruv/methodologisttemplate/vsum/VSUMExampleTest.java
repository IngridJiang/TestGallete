package tools.vitruv.methodologisttemplate.vsum;

import tools.vitruv.framework.vsum.VirtualModelBuilder;
import tools.vitruv.framework.vsum.internal.InternalVirtualModel;
import tools.vitruv.methodologisttemplate.model.model2.Model2Factory;
import tools.vitruv.methodologisttemplate.model.model.AscetModule;
import tools.vitruv.change.interaction.UserInteractor;
import tools.vitruv.change.interaction.UserInteractionOptions.WindowModality;
import tools.vitruv.framework.vsum.internal.VirtualModelImpl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Files;  
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.EObject;  
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import mir.reactions.amalthea2ascet.Amalthea2ascetChangePropagationSpecification;
import tools.vitruv.change.propagation.ChangePropagationMode;
import tools.vitruv.change.testutils.TestUserInteraction;
import tools.vitruv.framework.views.CommittableView;
import tools.vitruv.framework.views.View;
import tools.vitruv.framework.views.ViewTypeFactory;
import tools.vitruv.framework.vsum.VirtualModel;
import tools.vitruv.framework.vsum.internal.InternalVirtualModel;
import tools.vitruv.methodologisttemplate.model.model2.ComponentContainer;

/**
 * This class provides an example how to define and use a VSUM.
 */
public class VSUMExampleTest {

  @BeforeAll
  static void setup() {
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());
  }

  @Test
  void componentContainerInsertionAndPropagationTest(@TempDir Path tempDir) {
    VirtualModel vsum = createDefaultVirtualModel(tempDir);
    addComponentContainer(vsum, tempDir);
    // assert that the directly added System is present
    Assertions.assertEquals(1, getDefaultView(vsum, List.of(ComponentContainer.class)).getRootObjects().size());
    // as well as the Root that should be created by the Reactions, see templateReactions.reactions#14
    Assertions.assertEquals(1, getDefaultView(vsum, List.of(AscetModule.class)).getRootObjects().size());
  }

  @Test
  void insertTask(@TempDir Path tempDir) {
    InternalVirtualModel vsum = createDefaultVirtualModel(tempDir);
    addComponentContainer(vsum, tempDir);
    addTask(vsum);
    Assertions.assertTrue(assertView(getDefaultView(vsum, List.of(ComponentContainer.class, AscetModule.class)), (View v) -> {
       //assert that a component has been inserted, a entity has been created and that both have the same name
       //Note: to make the test result easier to understand, these different effects should be tested one by one
       return v.getRootObjects(ComponentContainer.class).iterator().next()
        .getTasks().get(0).getName()
        .equals(v.getRootObjects(AscetModule.class).iterator().next()
        .getTasks().get(0).getName());
    }));
    try {
    Path outDir = Paths.get("target", "generated-models");
    Files.createDirectories(outDir);
    mergeAndSave(vsum, outDir, "vsum.xmi");

for (Resource r : vsum.getViewSourceModels()) {
    Path src = Paths.get(java.net.URI.create(r.getURI().toString()));   
    Path dest = outDir.resolve(src.getFileName());
    Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
}

    } catch (IOException e) {
    Assertions.fail("Could not save generated model", e);
    }

  }
 
private void addComponentContainer(VirtualModel vsum, Path projectPath) {
    CommittableView view = getDefaultView(vsum, List.of(ComponentContainer.class)).withChangeDerivingTrait();
    modifyView(view, (CommittableView v) -> {
      v.registerRoot(
          Model2Factory.eINSTANCE.createComponentContainer(),
          URI.createFileURI(projectPath.toString() + "/example.model"));
    });

  }

  private void addTask(VirtualModel vsum) {
    CommittableView view = getDefaultView(vsum, List.of(ComponentContainer.class)).withChangeDerivingTrait();
    modifyView(view, (CommittableView v) -> {
      var task = Model2Factory.eINSTANCE.createTask();
      task.setName("specialname");
      v.getRootObjects(ComponentContainer.class).iterator().next().getTasks().add(task);
    });
  }


  private InternalVirtualModel createDefaultVirtualModel(Path projectPath) {
    var userInteractor = new TestUserInteraction();
    userInteractor.addNextSingleSelection(0);
    

    InternalVirtualModel model = new VirtualModelBuilder()
        .withStorageFolder(projectPath) 
        .withUserInteractorForResultProvider(new TestUserInteraction.ResultProvider(userInteractor))
        .withChangePropagationSpecifications(new Amalthea2ascetChangePropagationSpecification())
        .buildAndInitialize();
    model.setChangePropagationMode(ChangePropagationMode.TRANSITIVE_CYCLIC);
    return model;
  }

  // See https://github.com/vitruv-tools/Vitruv/issues/717 for more information about the rootTypes
  private View getDefaultView(VirtualModel vsum, Collection<Class<?>> rootTypes) {
    var selector = vsum.createSelector(ViewTypeFactory.createIdentityMappingViewType("default"));
    selector.getSelectableElements().stream()
      .filter(element -> rootTypes.stream().anyMatch(it -> it.isInstance(element)))
      .forEach(it -> selector.setSelected(it, true));
    return selector.createView();
  }

  // These functions are only for convience, as they make the code a bit better readable
  private void modifyView(CommittableView view, Consumer<CommittableView> modificationFunction) {
    modificationFunction.accept(view);
    view.commitChanges();
  }

  private boolean assertView(View view, Function<View, Boolean> viewAssertionFunction) {
    return viewAssertionFunction.apply(view);
  }

private static void mergeAndSave(InternalVirtualModel vm,
                                 Path outDir,
                                 String fileName) throws IOException {

    Files.createDirectories(outDir);

    ResourceSet rs = new ResourceSetImpl();
    URI mergedUri = URI.createFileURI(outDir.resolve(fileName).toString());
    Resource merged = rs.createResource(mergedUri);


    for (Resource src : vm.getViewSourceModels()) {
        for (EObject obj : src.getContents()) {
            merged.getContents().add(EcoreUtil.copy(obj));
        }
    }

    Map<String, Object> opts = Map.of(
        XMLResource.OPTION_ENCODING, "UTF-8",
        XMLResource.OPTION_FORMATTED, Boolean.TRUE,
        XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE
    );
    merged.save(opts);
}

}
