package android.ListenToMe.LanguageAssess.Bean;

import java.util.List;

public class Question {
    private Integer order;
    private String questionStr;
    private List<Option> options;
    private ABC_dimension dimension;

    public ABC_dimension getDimension() {
        return dimension;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getQuestionStr() {
        return questionStr;
    }

    public void setQuestionStr(String questionStr) {
        this.questionStr = questionStr;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }


}
//感觉（Sensory）、交往（Relating）、躯体和物体使用（BodyAndObjectUse）、语言（Language）以及社会生活自理（SocialAndSelfHelp）
enum ABC_dimension{
    Sensory,Relating,BodyAndObjectUse,Language,SocialAndSelfHelp;
}