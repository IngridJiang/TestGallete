#!/bin/bash

# Script to clean up old VSUM data files that contain hardcoded Windows paths
# This allows the application to regenerate the files with correct paths

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
GALETTE_OUTPUT_DIR="$SCRIPT_DIR/galette-output-0"

echo "Cleaning Galette VSUM data files..."

if [ -d "$GALETTE_OUTPUT_DIR" ]; then
    echo "Found existing galette-output-0 directory"
    
    # Remove files that contain hardcoded paths
    if [ -f "$GALETTE_OUTPUT_DIR/vsum/models.models" ]; then
        echo "Removing models.models (contains hardcoded paths)"
        rm "$GALETTE_OUTPUT_DIR/vsum/models.models"
    fi
    
    if [ -f "$GALETTE_OUTPUT_DIR/vsum/uuid.uuid" ]; then
        echo "Removing uuid.uuid (contains hardcoded paths)"
        rm "$GALETTE_OUTPUT_DIR/vsum/uuid.uuid"
    fi
    
    if [ -f "$GALETTE_OUTPUT_DIR/vsum/correspondences.correspondence" ]; then
        echo "Removing correspondences.correspondence"
        rm "$GALETTE_OUTPUT_DIR/vsum/correspondences.correspondence"
    fi
    
    # Keep the model files but let VSUM regenerate the metadata
    echo "Keeping model files: example.model and example.model2"
    echo "VSUM will regenerate metadata files with correct paths"
else
    echo "No galette-output-0 directory found - will be created on first run"
fi

echo "Cleanup complete!"
echo ""
echo "Note: You can run this script anytime to reset VSUM data if path issues occur."