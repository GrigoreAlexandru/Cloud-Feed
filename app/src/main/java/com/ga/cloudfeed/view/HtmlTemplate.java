package com.ga.cloudfeed.view;


import com.ga.cloudfeed.model.ItemModel;

public final class HtmlTemplate {

    public String getHtml(ItemModel item, String date){
        String content = item.encoded != null ? item.encoded : item.description;
        String image = (content.length() > 50 ? content.substring(0, 50) : content)
                               .contains("<img") ? "" : ("<img " + "src='"+item.image+"'>");
        return  "<!DOCTYPE html>" +
                "<html>\n" +
                "<head><link rel='stylesheet' type='text/css' href='style.css' /></head>\n" +
                "<link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/icon?family=Material+Icons\">" +
                "<body class='body'>" +
                    "<div class='header'>" +
                        "<h1 class='title'>"+item.title+"</h1>" +
                        "<p class='subheader'>Author: "+ (item.author == null ? "unknown" : item.author) +" Date: "+date+"</p>" +
                    "</div>"+
                        image +
                    "<div class='content'>" +
                        content +
                    "</div>" +
                    "<div class='buttons'>" +
                        "<button class='button mdl-button mdl-js-button mdl-js-ripple-effect' type='button' onClick='window.JSInterface.share();'>SHARE " +
                            "<i class='material-icons'>share</i>" +
                        "</button>" +
                        "<button class='button mdl-button mdl-js-button mdl-js-ripple-effect' type='button mdl-button mdl-js-button mdl-js-ripple-effect' " +
                            "onClick='window.JSInterface.visit();'>VISIT WEBSITE</button>" +
                    "</div>" +
                "</body>\n" +
                "</html>";

    }
}
