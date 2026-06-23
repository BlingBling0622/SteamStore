package com.steamlibrary.config;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Locale;

@ControllerAdvice
public class GlobalModelAdvice {

    @ModelAttribute
    public void addCurrency(Locale locale, Model model) {
        model.addAttribute("currentLang", locale.getLanguage());
        switch (locale.getLanguage()) {
            case "ja" -> { model.addAttribute("cSym", "¥");  model.addAttribute("cRate", 155.0);  model.addAttribute("cDec", 0); }
            case "ko" -> { model.addAttribute("cSym", "₩");  model.addAttribute("cRate", 1370.0); model.addAttribute("cDec", 0); }
            case "ru" -> { model.addAttribute("cSym", "₽");  model.addAttribute("cRate", 89.0);   model.addAttribute("cDec", 0); }
            case "fr", "de", "es" ->
                         { model.addAttribute("cSym", "€");  model.addAttribute("cRate", 0.92);   model.addAttribute("cDec", 2); }
            case "zh" -> { model.addAttribute("cSym", "¥");  model.addAttribute("cRate", 7.25);   model.addAttribute("cDec", 2); }
            default   -> { model.addAttribute("cSym", "$");  model.addAttribute("cRate", 1.0);    model.addAttribute("cDec", 2); }
        }
    }
}
