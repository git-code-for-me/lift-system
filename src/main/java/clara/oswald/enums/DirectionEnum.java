package clara.oswald.enums;

public enum DirectionEnum {

    UP,DOWN,IDLE;

    public static DirectionEnum getDirection(String direction) {
        for (DirectionEnum value : DirectionEnum.values()) {
            if (value.name().equals(direction)) {
                return value;
            }
        }
        return null;
    }
}
