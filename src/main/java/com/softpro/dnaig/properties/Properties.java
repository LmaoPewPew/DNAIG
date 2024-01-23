package com.softpro.dnaig.properties;

import com.softpro.dnaig.utils.Config;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;


public interface Properties {
    Config.type categoryType = null;
    String name = null;
    String id = null;
    String[] pos = new String[3];
    String[] rot = new String[3];
    ImageView imageView = null;
    Button button = null;

    //GET
    Button getButton();
    String getId();
    String getName();
    String[] getPos();
    String[] getRot();

    //SET
    void setButton(Button button);
    void setId(String id);
    void setName(String name);
    void setPos(String[] pos);
    void setRot(String[] rot);


}
