package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Controller
public class vCardServices {
    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="Szkoła") String name, Model model) throws IOException {
        model.addAttribute("name", name);

        String stringBuilder = "https://panoramafirm.pl/szukaj?k=" + name + "&l=";
        Document doc = Jsoup.connect(stringBuilder).get();

        List<Firma> firmy = new ArrayList<>();
        for (Element element : doc.select("script")) {
            if (element.attr("type").equals("application/ld+json")) {
                if (element.data().contains("LocalBusiness")) {
                    Firma firma = new Firma(element);

                    firmy.add(firma);
                }
            }
        }
        return "greeting";
    }
}
