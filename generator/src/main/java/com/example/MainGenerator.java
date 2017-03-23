package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Property;
import org.greenrobot.greendao.generator.Schema;

public class MainGenerator
{
    public static void main(String[] args)
            throws Exception
    {
        final Schema       schema;
        final Entity       restEntity;
        final Entity        parksEntity;
        final Entity        artEntity;
        final Entity        cafeEntity;
        final Entity        barEntity;
        final DaoGenerator generator;

        schema = new Schema(1, "a00971903.comp3717.ca.bcit.ca.explorenewwest.database.schema");
        restEntity = schema.addEntity("Restaurant");
        restEntity.addIdProperty();
        restEntity.addStringProperty("name").notNull();
        restEntity.addDoubleProperty("latitude").notNull();
        restEntity.addDoubleProperty("longitude").notNull();
        restEntity.addLongProperty("category").notNull();

        parksEntity = schema.addEntity("Park");
        parksEntity.addIdProperty();
        parksEntity.addStringProperty("name").notNull();
        parksEntity.addDoubleProperty("latitude").notNull();
        parksEntity.addDoubleProperty("longitude").notNull();

        artEntity = schema.addEntity("Art");
        artEntity.addIdProperty();
        artEntity.addStringProperty("name").notNull();
        artEntity.addDoubleProperty("latitude").notNull();
        artEntity.addDoubleProperty("longitude").notNull();

        cafeEntity = schema.addEntity("Cafe");
        cafeEntity.addIdProperty();
        cafeEntity.addStringProperty("name").notNull();
        cafeEntity.addDoubleProperty("latitude").notNull();
        cafeEntity.addDoubleProperty("longitude").notNull();


        barEntity = schema.addEntity("Bar");
        barEntity.addIdProperty();
        barEntity.addStringProperty("name").notNull();
        barEntity.addDoubleProperty("latitude").notNull();
        barEntity.addDoubleProperty("longitude").notNull();

        /**ToMany categoryToData = categoryEntity.addToMany(dataEntity,categoryID);
         categoryToData.setName("datasets");

         ToOne dataToCategory = dataEntity.addToOne(categoryEntity,categoryID);
         dataToCategory.setName("category");**/




        generator = new DaoGenerator();
        generator.generateAll(schema, "./app/src/main/java");
    }
}
