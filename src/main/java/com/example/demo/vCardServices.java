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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
        StringBuilder[] vcfBuilder = new StringBuilder[firmy.size()];

        for (int i = 0; i < firmy.size(); i++) {
            vcfBuilder[i] = new StringBuilder("BEGIN:VCARD\r\n");
            vcfBuilder[i].append("VERSION:4.0\r\n");
            vcfBuilder[i].append("ORG:").append(firmy.get(i).getName()).append("\n");
            vcfBuilder[i].append("TEL:").append(firmy.get(i).getTelephone()).append("\n");
            vcfBuilder[i].append("END:VCARD\n");
            File file = new File("src/main/resources/card/" + firmy.get(i).getTelephone() + ".vcf");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(vcfBuilder[i].toString().getBytes(StandardCharsets.UTF_8));
        }

        model.addAttribute("List",firmy);
        return "greeting";
    }
}
