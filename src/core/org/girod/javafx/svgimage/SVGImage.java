/*
Copyright (c) 2021, Hervé Girod
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

3. Neither the name of the copyright holder nor the names of its
   contributors may be used to endorse or promote products derived from
   this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

Alternatively if you have any questions about this project, you can visit
the project website at the project page on https://github.com/hervegirod/fxsvgimage
 */
package org.girod.javafx.svgimage;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

/**
 * The resulting SVG image.It is a JavaFX Nodes tree.
 *
 * @version 0.2
 */
public class SVGImage extends Group {
   private static SnapshotParameters SNAPSHOT_PARAMS = null;
   private final Map<String, Node> nodes = new HashMap<>();

   /**
    * Set the default SnapshotParameters to use when creating a snapshot. The default is null, which means that a
    * default SnapshotParameters will be created when creating a snapshot.
    *
    * @param params the default SnapshotParameters
    */
   public static void setDefaultSnapshotParameters(SnapshotParameters params) {
      SNAPSHOT_PARAMS = params;
   }

   /**
    * Return the default SnapshotParameters used when creating a snapshot.
    *
    * @return the default SnapshotParameters
    */
   public static SnapshotParameters getDefaultSnapshotParameters() {
      return SNAPSHOT_PARAMS;
   }

   void putNode(String id, Node node) {
      nodes.put(id, node);
   }

   /**
    * Return the Node indicated by id.
    *
    * @param id the name of the Node
    * @return the Node
    */
   public Node getNode(String id) {
      return nodes.get(id);
   }

   /**
    * Return the width of the image.
    *
    * @return the width
    */
   public double getWidth() {
      return this.getLayoutBounds().getWidth();
   }

   /**
    * Return the height of the image.
    *
    * @return the height
    */
   public double getHeight() {
      return this.getLayoutBounds().getHeight();
   }

   /**
    * Convert the Node tree to an image.
    *
    * @param scale the scale
    * @return the Image
    */
   public Image toImageScaled(double scale) {
      return toImageScaled(scale, scale);
   }

   /**
    * Convert the Node tree to an image.
    *
    * @param scaleX the X scale
    * @param scaleY the Y scale
    * @return the Image
    */
   public Image toImageScaled(double scaleX, double scaleY) {
      this.setScaleX(scaleX);
      this.setScaleY(scaleY);
      SnapshotParameters params = SNAPSHOT_PARAMS;
      if (params == null) {
         params = new SnapshotParameters();
      }
      WritableImage image = this.snapshot(params, null);
      return image;
   }

   /**
    * Convert the Node tree to an image, specifying the resulting width and preserving the image ratio.
    *
    * @param width the resulting width
    * @return the Image
    */
   public Image toImage(double width) {
      double initialWidth = this.getLayoutBounds().getWidth();
      double initialHeight = this.getLayoutBounds().getHeight();
      double scaleX = width / initialWidth;
      double scaleY = initialHeight * scaleX;
      this.setScaleX(scaleX);
      this.setScaleY(scaleY);
      SnapshotParameters params = SNAPSHOT_PARAMS;
      if (params == null) {
         params = new SnapshotParameters();
      }
      WritableImage image = this.snapshot(params, null);
      return image;
   }

   /**
    * Convert the Node tree to an image, without applying a scale.
    *
    * @return the Image
    */
   public Image toImage() {
      SnapshotParameters params = SNAPSHOT_PARAMS;
      if (params == null) {
         params = new SnapshotParameters();
      }
      WritableImage image = this.snapshot(params, null);
      return image;
   }

   /**
    * Convert the Node tree to an image.
    *
    * @param params the parameters
    * @return the Image
    */
   public Image toImage(SnapshotParameters params) {
      WritableImage image = this.snapshot(params, null);
      return image;
   }
}
