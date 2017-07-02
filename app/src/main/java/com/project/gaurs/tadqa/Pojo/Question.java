package com.project.gaurs.tadqa.Pojo;

import java.util.ArrayList;

/**
 * Created by gaurs on 5/29/2017.
 */

public class Question {
    String ques;
    ArrayList<Child> Items;
    String answer;
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public ArrayList<Child> getItems() {
        return Items;
    }

    public void setItems(ArrayList<Child> Items) {
        this.Items = Items;
    }

    public Question(String ques, ArrayList<Child> items) {
        this.ques = ques;
        Items = items;
    }

    public Question() {

    }

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

}
