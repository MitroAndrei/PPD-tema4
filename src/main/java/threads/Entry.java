package threads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Entry {
    private int id;
    private int score;

    public void addScore(int score) {
        this.score += score;
    }

}
