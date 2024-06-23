package android.ListenToMe.LanguageAssess.Bean;

public class Option {
    private String optionStr;
    private Integer score;
    private ABC_dimension dimension;

    public ABC_dimension getDimension() {
        return dimension;
    }

    public void setDimension(ABC_dimension dimension) {
        this.dimension = dimension;
    }

    public String getOptionStr() {
        return optionStr;
    }

    public void setOptionStr(String optionStr) {
        this.optionStr = optionStr;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}