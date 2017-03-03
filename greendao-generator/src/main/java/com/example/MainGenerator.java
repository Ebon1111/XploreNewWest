package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MainGenerator {
    public static void main(String[] args)
            throws Exception
    {
        final Schema schema;
        final Entity categories;
        final Entity locations;
        final DaoGenerator generator;

        schema = new Schema(1, "a00971903.comp3717.ca.bcit.ca.explorenewwest.database.schema");

        categories = schema.addEntity("Category");
        categories.addIdProperty();
        categories.addLongProperty("categoryID").notNull();
        categories.addStringProperty("name").notNull();

        locations = schema.addEntity("Locations");
        locations.addIdProperty();
        locations.addStringProperty("name").notNull();
        locations.addLongProperty("latitude").notNull();
        locations.addLongProperty("longitude").notNull();
        locations.addLongProperty("categoryID").notNull();



        generator = new DaoGenerator();
        generator.generateAll(schema, "./app/src/main/java");
    }
}
