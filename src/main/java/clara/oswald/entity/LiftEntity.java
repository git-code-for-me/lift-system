package clara.oswald.entity;

/**
 * 电梯轿厢
 */
public class LiftEntity {
    /**
     * 当前所在楼层
     */
    private Integer n;

    /**
     * 轿厢容量
     */
    private Integer capacity;


    public boolean isOverLoad(){
        return false;
    }
}
