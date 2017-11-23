

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;

public class Tlg extends TelegramLongPollingBot{

    public static void tlgInit() {
        ApiContextInitializer.init(); // Инициализируем апи
        TelegramBotsApi botapi = new TelegramBotsApi();
        try {
            botapi.registerBot(new Tlg());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String getBotUsername() {
        return "shvarkBot";
        //возвращаем юзера
    }

    @Override
    public void onUpdateReceived(Update e) {
        // Тут будет то, что выполняется при получении сообщения
        Message msg = e.getMessage(); // Это нам понадобится
        String txt = msg.getText();
        switch(txt) {
            case "/wrd":
                Cam.wrd_init = true;
                sendMsg(msg, "Окей, начинаем вардить, как только чет увижу свистну!");
                break;
            case "gid":
                System.out.println(msg.getChatId());
                break;
            case "/ewrd":
                Cam.wrd_init = false;
                sendMsg(msg, "Окей, больше не вардим!");
                break;
            case "/del":
                System.out.println(msg.getChatId());
                deleteAllFilesFromPath("./phs/");
                sendMsg(msg, "Все фото из вместилища очищены!");
                break;
            default:
                sendMsg(msg, "я хз братан - \""+txt+"\" - че это?");
                break;
        }
    }


    @SuppressWarnings("deprecation") // Означает то, что в новых версиях метод уберут или заменят
    public void sendMsg(Message msg, String text) {
        SendMessage s = new SendMessage();
        s.setChatId(msg.getChatId()); // Боту может писать не один человек, и поэтому чтобы отправить сообщение, грубо говоря нужно узнать куда его отправлять
        s.setText(text);
        try { //Чтобы не крашнулась программа при вылете Exception
            sendMessage(s);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation") // Означает то, что в новых версиях метод уберут или заменят
    public void sendMsg(String text) {
        SendMessage s = new SendMessage();
        s.setChatId("@megaShirvan"); // Боту может писать не один человек, и поэтому чтобы отправить сообщение, грубо говоря нужно узнать куда его отправлять
        s.setText(text);
        try { //Чтобы не крашнулась программа при вылете Exception
            sendMessage(s);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }

    @Override
    public String getBotToken() {
        return "476871764:AAEE_jxroAgUuU2mjC14lq3desSLPy7xDec";
        //Токен бота
    }
    static void getFilesFromDirectory(String path) {
        // создаем объект File для каталога
        File parsDir = new File(path);
        //получаем массив ссылок
        File[] filesLinks = parsDir.listFiles();
        if(filesLinks.length !=0) {
            for (int linkNumber = 0; linkNumber < filesLinks.length; linkNumber++) {
                //если это файл то делаем грязь
                if (filesLinks[linkNumber].isFile()) {
                    System.out.println("Файл:" + filesLinks[linkNumber].getName() + " Размером:" + filesLinks[linkNumber].length() + " байт");
                }//если это каталог то ищем там файлы
                else {
                    getFilesFromDirectory(filesLinks[linkNumber].getAbsolutePath());
                }
            }
            //удаляем все файлы
            deleteAllFilesFromPath(path);
        }
        else {
            System.out.println("Пусто");
        }

    }
    static void deleteAllFilesFromPath(String path) {
        // создаем объект File для каталога
        File parsDir = new File(path);
        //получаем массив ссылок
        File[] filesLinks = parsDir.listFiles();
        if(filesLinks.length !=0) {
            for (int linkNumber = 0; linkNumber < filesLinks.length; linkNumber++) {
                //если это файл то делаем грязь
                if (filesLinks[linkNumber].isFile()) {
                    filesLinks[linkNumber].delete();
                }//если это каталог то ищем там файлы
                else {
                    getFilesFromDirectory(filesLinks[linkNumber].getAbsolutePath());
                }
            }
        }
        else {
            System.out.println("Пусто");
        }

    }
    public static String sendMessageTo(String msg){
        String response = HttpRequest.get("https://api.telegram.org/bot476871764:AAEE_jxroAgUuU2mjC14lq3desSLPy7xDec/sendMessage?chat_id=120988325&text=" + msg).body();
        System.out.println("Response was: " + response);
        return response;
    }
    public static void sendPhotoTo(String path, String chat_id){
        Http http = new Http();
        http.sendPhotoTo(new File(path), chat_id);
    }
}
