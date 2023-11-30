package com.softpro.dnaig.properties;
import com.softpro.dnaig.utils.Config;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.util.function.Consumer;


public interface Properties {
    Config.type categoryType = null;
    String name = null;
    String id = null;
    String[] pos = new String[3];
    String[] rot = new String[3];
    ImageView imageView = null;
    Button button = null;
    String getId();
    Button getButton();

    String getName();

    String[] getPos();

    String[] getRot();
    void setButton(Button button);

}
