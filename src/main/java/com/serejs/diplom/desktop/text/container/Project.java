package com.serejs.diplom.desktop.text.container;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.text.SimpleDateFormat;

@AllArgsConstructor
@Data
public class Project implements JsonSerializable {
    private Long id;
    private String title;
    private View view;

    private static final SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy");
    private Date date;

    @Override
    public JsonObject toJson() {
        Gson gson = new Gson();
        var projectObj = gson.toJsonTree(this).getAsJsonObject();
        projectObj.remove("view");
        projectObj.remove("date");

        JsonObject nestedJson = null;
        if (view != null) {
            nestedJson = new JsonObject();
            nestedJson.addProperty("id", view.getId());
        }

        projectObj.add("view", nestedJson);

        return projectObj;
    }

    @Override
    public String toString() {
        return title;
    }

    public String getDate() {
        return sfd.format(date);
    }
}
