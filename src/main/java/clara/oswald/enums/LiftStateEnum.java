package clara.oswald.enums;

public enum LiftStateEnum {

    IDLE,RUNNING,BREAK;

    public Boolean normal(){
        return this.equals(RUNNING)||this.equals(IDLE);
    }
}
