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
package org.girod.javafx.svgimage.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.transform.Transform;

/**
 * Parser utilities for transforms.
 *
 * @since 0.6
 */
public class TransformUtils implements SVGTags {
   private static final Pattern TRANSFORM_PAT = Pattern.compile("\\w+\\((.*)\\)");

   private TransformUtils() {
   }

   private static List<Double> getTransformArguments(String transformTxt, Viewport viewport) {
      List<Double> args = new ArrayList<>();
      Matcher m = TRANSFORM_PAT.matcher(transformTxt);
      if (m.matches()) {
         String content = m.group(1);
         StringTokenizer tok = new StringTokenizer(content, ", ");
         while (tok.hasMoreTokens()) {
            String argumentS = tok.nextToken();
            ParserUtils.parseLengthValue(args, argumentS, true, null, viewport);
         }
      } else {
         return null;
      }
      return args;
   }

   /**
    * Set the transforms for a node if it has the {@link #TRANSFORM} attribute.
    *
    * @param node the node
    * @param xmlNode the xml node
    * @param viewport the viewport
    */
   public static void setTransforms(Node node, XMLNode xmlNode, Viewport viewport) {
      if (xmlNode.hasAttribute(TRANSFORM)) {
         String transforms = xmlNode.getAttributeValue(TRANSFORM);
         List<Transform> transformList = extractTransforms(transforms, viewport);
         if (!transformList.isEmpty()) {
            ObservableList<Transform> nodeTransforms = node.getTransforms();
            Iterator<Transform> it = transformList.iterator();
            while (it.hasNext()) {
               Transform theTransForm = it.next();
               nodeTransforms.add(theTransForm);
            }
         }
      }
   }

   /**
    * Extract the transforms from a {@link #TRANSFORM} attribute
    *
    * @param transforms the transform attribute.
    * @param viewport the viewport
    * @return the list of transforms
    */
   public static List<Transform> extractTransforms(String transforms, Viewport viewport) {
      List<Transform> transformList = new ArrayList<>();

      StringTokenizer tokenizer = new StringTokenizer(transforms, ")");
      while (tokenizer.hasMoreTokens()) {
         String transformTxt = tokenizer.nextToken() + ")";
         transformTxt = transformTxt.trim();
         if (transformTxt.startsWith("translate(")) {
            List<Double> args = getTransformArguments(transformTxt, viewport);
            if (args.size() == 2) {
               Transform transform = Transform.translate(args.get(0), args.get(1));
               transformList.add(transform);
            }
         } else if (transformTxt.startsWith("translateX(")) {
            List<Double> args = getTransformArguments(transformTxt, viewport);
            if (args.size() == 1) {
               Transform transform = Transform.translate(args.get(0), 0);
               transformList.add(transform);
            }
         } else if (transformTxt.startsWith("translateY(")) {
            List<Double> args = getTransformArguments(transformTxt, viewport);
            if (args.size() == 1) {
               Transform transform = Transform.translate(0, args.get(0));
               transformList.add(transform);
            }
         } else if (transformTxt.startsWith("scale(")) {
            List<Double> args = getTransformArguments(transformTxt, viewport);
            if (args.size() == 2) {
               Transform transform = Transform.scale(args.get(0), args.get(1));
               transformList.add(transform);
            }
         } else if (transformTxt.startsWith("scaleX(")) {
            List<Double> args = getTransformArguments(transformTxt, viewport);
            if (args.size() == 1) {
               Transform transform = Transform.translate(args.get(0), 1);
               transformList.add(transform);
            }
         } else if (transformTxt.startsWith("scaleY(")) {
            List<Double> args = getTransformArguments(transformTxt, viewport);
            if (args.size() == 1) {
               Transform transform = Transform.translate(1, args.get(0));
               transformList.add(transform);
            }
         } else if (transformTxt.startsWith("rotate(")) {
            List<Double> args = getTransformArguments(transformTxt, viewport);
            if (args.size() == 3) {
               Transform transform = Transform.rotate(args.get(0), args.get(1), args.get(2));
               transformList.add(transform);
            } else if (args.size() == 1) {
               Transform transform = Transform.rotate(args.get(0), 0, 0);
               transformList.add(transform);
            }
         } else if (transformTxt.startsWith("skewX(")) {
            List<Double> args = getTransformArguments(transformTxt, viewport);
            if (args.size() == 1) {
               Transform transform = Transform.shear(args.get(0), 1);
               transformList.add(transform);
            }
         } else if (transformTxt.startsWith("skewY(")) {
            List<Double> args = getTransformArguments(transformTxt, viewport);
            if (args.size() == 1) {
               Transform transform = Transform.shear(1, args.get(0));
               transformList.add(transform);
            }
         } else if (transformTxt.startsWith("matrix(")) {
            List<Double> args = getTransformArguments(transformTxt, viewport);
            if (args.size() == 6) {
               Transform transform = Transform.affine(args.get(0), args.get(1), args.get(2), args.get(3), args.get(4), args.get(5));
               transformList.add(transform);
            }
         }
      }

      return transformList;
   }
}