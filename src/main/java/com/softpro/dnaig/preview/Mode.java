package com.softpro.dnaig.preview;

/**
 * Enumeration class representing different modes of movement for Camera and Objects.
 * Each mode encapsulates a specific action and target type.
 */
public enum Mode {

    ERROR("01100101 01110010 01110010 01101111 01110010", TargetType.ERROR, ActionType.ERROR),
    MOVE_CAMERA_XY("move camera (xy)", TargetType.CAMERA, ActionType.MOVE),
    MOVE_CAMERA_X("move camera (x)", TargetType.CAMERA, ActionType.MOVE),
    MOVE_CAMERA_Y("move camera (y)", TargetType.CAMERA, ActionType.MOVE),
    MOVE_CAMERA_Z("move camera (z)", TargetType.CAMERA, ActionType.MOVE),
    MOVE_OBJECT_XY("move object (xy)", TargetType.OBJECT, ActionType.MOVE),
    MOVE_OBJECT_X("move object (x)", TargetType.OBJECT, ActionType.MOVE),
    MOVE_OBJECT_Y("move object (y)", TargetType.OBJECT, ActionType.MOVE),
    MOVE_OBJECT_Z("move object (z)", TargetType.OBJECT, ActionType.MOVE),
    ROTATE_CAMERA_XY("rotate camera (xy)", TargetType.CAMERA, ActionType.ROTATE),
    ROTATE_CAMERA_X("rotate camera (x)", TargetType.CAMERA, ActionType.ROTATE),
    ROTATE_CAMERA_Y("rotate camera (y)", TargetType.CAMERA, ActionType.ROTATE),
    ROTATE_CAMERA_Z("rotate camera (z)", TargetType.CAMERA, ActionType.ROTATE),
    ROTATE_OBJECT_XY("rotate object (xy)", TargetType.OBJECT, ActionType.ROTATE),
    ROTATE_OBJECT_X("rotate object (x)", TargetType.OBJECT, ActionType.ROTATE),
    ROTATE_OBJECT_Y("rotate object (y)", TargetType.OBJECT, ActionType.ROTATE),
    ROTATE_OBJECT_Z("rotate object (z)", TargetType.OBJECT, ActionType.ROTATE),
    ;

    private final String name;
    private final TargetType targetType;
    private final ActionType actionType;

    /**
     * Initializes a new Mode object with the specified name, target type, and action type.
     *
     * @param name The name of the mode.
     * @param targetType The target type of the mode (CAMERA, OBJECT, or ERROR).
     * @param actionType The action type of the mode (MOVE, ROTATE, or ERROR).
     */
    Mode(String name, TargetType targetType, ActionType actionType) {
        this.name = name;
        this.targetType = targetType;
        this.actionType = actionType;
    }

    /**
     * Returns the name of the mode.
     *
     * @return The name of the mode.
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Returns the target type of the current mode.
     *
     * @return The target type of the current mode.
     */
    public TargetType getTargetType() {
        return targetType;
    }

    /**
     * Enumeration class representing different target types.
     *
     * <p>Possible values are CAMERA, OBJECT, and ERROR.
     */
    protected enum TargetType {
        CAMERA,
        OBJECT,
        ERROR
    }

    /**
     * Enumeration class representing different action types.
     *
     * <p>The possible action types are MOVE, ROTATE, and ERROR.
     */
    protected enum ActionType {
        MOVE,
        ROTATE,
        ERROR
    }
}