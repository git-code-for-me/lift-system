package clara.oswald.entity;

import clara.oswald.enums.DirectionEnum;
import lombok.Getter;
import lombok.ToString;

@ToString
public class Task {

    @Getter
    private Integer n;

    public Task(Integer n, DirectionEnum direction) {
        this.n = n;
        this.direction = direction;
    }

    @Getter
    private DirectionEnum direction;

    public Task(Integer n, String direction) {
        this.n = n;
        this.direction = DirectionEnum.getDirection(direction);
    }

}
