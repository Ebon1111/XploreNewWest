package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MainGenerator {
    public static void main(String[] args)
            throws Exception
    {
        final Schema schema;
        final Entity cafeEntity;
        final DaoGenerator generator;

        schema = new Schema(1, "a00971903.comp3717.ca.bcit.ca.explorenewwest.database.schema");

        // Cafe Table
        cafeEntity = schema.addEntity("cafe");
        cafeEntity.addIdProperty();
        cafeEntity.addStringProperty("cafeName").notNull();
        cafeEntity.addStringProperty("cafeAddress").notNull();
        cafeEntity.addLongProperty("latitude").notNull();
        cafeEntity.addLongProperty("longitude").notNull();

        generator = new DaoGenerator();
        generator.generateAll(schema, "./app/src/main/java");
    }
}
