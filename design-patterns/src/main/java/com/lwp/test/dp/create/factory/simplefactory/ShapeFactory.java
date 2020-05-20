package com.lwp.test.dp.create.factory.simplefactory;

import com.lwp.test.dp.create.factory.Circle;
import com.lwp.test.dp.create.factory.Shape;

/**
 * ShapeFactory
 *
 * @author lanwenping
 * @version 1.0
 * @date 2020/5/16 13:50
 */
public class ShapeFactory {

    public static Shape getShape(String shapeType) {
        if (shapeType == null) {
            return null;
        }
        if ("circle".equalsIgnoreCase(shapeType)) {
            return new Circle();
        } else if ("rectangle".equalsIgnoreCase(shapeType)) {
            return new Circle();
        } else if ("circle".equalsIgnoreCase(shapeType)) {
            return new Circle();
        }
        return null;
    }
}
