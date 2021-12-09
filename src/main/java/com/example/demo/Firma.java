package com.example.demo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONObject;

import org.jsoup.nodes.Element;

@Getter
@Setter
@ToString
public class Firma {
    String name;
    String telephone;
    String email;
    String postalCode;
    String street;
    String city;
    public Firma(Element element) {
        this.name = GetFromJson(element.data(), "name");
        this.telephone = GetFromJson(element.data(), "telephone");
        this.email = GetFromJson(element.data(), "email");
        this.postalCode = GetFromJson(element.data(), "postalCode");
        this.street = GetFromJson(element.data(), "streetAddress");
        this.city = GetFromJson(element.data(), "addressLocality");
    }
    private String GetFromJson(String json, String field) {
        int indexOf = json.indexOf(field);
        return json.substring(indexOf + field.length() + 3, json.indexOf("\"", indexOf + field.length() + 3));
    }
}
