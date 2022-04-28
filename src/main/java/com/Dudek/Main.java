package com.Dudek;

import static spark.Spark.*;

import com.fasterxml.uuid.Generators;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import spark.Request;
import spark.Response;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class Main {

    static ArrayList<Car> cars = new ArrayList<>();

    public static void main(String[] args) {
        port(3000);
        staticFiles.location("static");
        post("/add", (req, res) -> addNewCar(req, res));
        post("/del", (req, res) -> deleteById(req));
        post("/edit", (req, res) -> editCar(req));
        get("/json", (req, res) -> displayJSONData(res));
        get("/generateCars", (req, res) -> generateCars());
        post("/get", (req, res) -> getCarById(req));
        post("/invoice", (req, res) -> createInvoice(req));
        get("/invoicedownload", (req, res) -> downloadInvoice(req, res));

        get("/json-pdf", (req, res) -> displayJSONDataxd(res));
        get("/invoiceall", (req, res) -> createInvoiceAll(req));
        get("/invoicedownloadall", (req, res) -> downloadInvoiceAll(req, res));
    }

    static String downloadInvoiceAll(Request req, Response res) throws IOException {
        String uuid = req.queryParams("uuid");
        res.type("application/octet-stream");
        res.header("Content-Disposition", "attachment; filename=faktura-wszystkie.pdf");

        OutputStream outputStream = res.raw().getOutputStream();
        outputStream.write(Files.readAllBytes(Path.of("invoices/"+ uuid +"invoiceall.pdf")));

        return "ok";
    }

    static String createInvoiceAll(Request req) throws FileNotFoundException, DocumentException {

        Document document = new Document();

        long unixTime = System.currentTimeMillis() / 1000L;
        System.out.println(unixTime);

        String path = "invoices/" + unixTime + "invoiceall.pdf";
        PdfWriter.getInstance(document, new FileOutputStream(path));

        int number = 0;
        float finalPrice = 0;

        document.open();

        Font font = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);

        Paragraph p = new Paragraph("FAKTURA VAT: " + LocalDate.now(), font);
        document.add(p);
        Paragraph nabywca = new Paragraph("Nabbywca: Jakaś firma", font);
        document.add(nabywca);
        Paragraph sprzedawca = new Paragraph("Sprzedawca: Inna firma", font);
        document.add(sprzedawca);
        Paragraph dane = new Paragraph("Faktura za wszystkie auta", font);
        document.add(dane);
        Paragraph puste = new Paragraph(" ", font);
        document.add(puste);

        PdfPTable table = new PdfPTable(5);

        PdfPCell lp = new PdfPCell(new Phrase("lp.", font));
        table.addCell(lp);
        PdfPCell nazwa = new PdfPCell(new Phrase("nazwa", font));
        table.addCell(nazwa);
        PdfPCell cena = new PdfPCell(new Phrase("cena", font));
        table.addCell(cena);
        PdfPCell vat = new PdfPCell(new Phrase("vat", font));
        table.addCell(vat);
        PdfPCell wartosc = new PdfPCell(new Phrase("wartosc", font));
        table.addCell(wartosc);


        for (Car car : cars) {
            PdfPCell id = new PdfPCell(new Phrase(String.valueOf(++number), font));
            table.addCell(id);
            PdfPCell lpc = new PdfPCell(new Phrase(car.getModel(), font));
            table.addCell(lpc);
            PdfPCell cenaCar = new PdfPCell(new Phrase(String.valueOf(car.getPrice()), font));
            table.addCell(cenaCar);
            float price = (float) (car.getPrice() + car.getPrice() * ( car.getVat() / 100.0 ));
            finalPrice += price;
            PdfPCell vatc = new PdfPCell(new Phrase(car.getVat() + "%", font));
            table.addCell(vatc);
            PdfPCell wartoscc = new PdfPCell(new Phrase(String.valueOf(price), font));
            table.addCell(wartoscc);
        }

        document.add(table);

        Chunk suma = new Chunk(String.valueOf(finalPrice), font);
        document.add(suma);

        document.close();
        return String.valueOf(unixTime);
    }


    static String displayJSONDataxd(Response res) {
        Type listType = new TypeToken<ArrayList<Car>>() {}.getType();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        res.type("application/json");
        return gson.toJson(cars, listType);
    }

    static String downloadInvoice(Request req, Response res) throws IOException {
        String uuid = req.queryParams("uuid");
        res.type("application/octet-stream");
        res.header("Content-Disposition", "attachment; filename="+ uuid +".pdf");

        OutputStream outputStream = res.raw().getOutputStream();
        outputStream.write(Files.readAllBytes(Path.of("invoices/"+ uuid +".pdf")));

        return "ok";
    }

    static String createInvoice(Request req) throws IOException, DocumentException {
        Gson gson = new Gson();
        System.out.println(req.body());
        String carModel = gson.fromJson(req.body(), Car.class).getModel();
        String carImage = gson.fromJson(req.body(), Car.class).getImage();
        String carColor = gson.fromJson(req.body(), Car.class).getColor();
        int carYear = gson.fromJson(req.body(), Car.class).getYear();
        ArrayList<Airbag> carAirbag = gson.fromJson(req.body(), Car.class).getAirbag();
        UUID uuid = gson.fromJson(req.body(), Car.class).getUUID();
        Document document = new Document();
        String path = "invoices/" + uuid + ".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(path));

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Paragraph p = new Paragraph("Faktura dla " + uuid, font);
        document.add(p);
        Paragraph date = new Paragraph("Data sprzedaży " + LocalDate.now(), font);
        document.add(date);
        Paragraph model = new Paragraph("Model: " + carModel, font);
        document.add(model);
        Paragraph color = new Paragraph("color: " + carColor, font);
        document.add(color);
        Paragraph year = new Paragraph("year: " + carYear, font);
        document.add(year);
        Paragraph bag = new Paragraph("poduszki: ", font);
        document.add(bag);
        for(Airbag airbag: carAirbag){
            Paragraph desc = new Paragraph(airbag.getDescription() + ": " + airbag.isValue(), font);
            document.add(desc);
        }
        Image img = Image.getInstance("src/main/resources/static/images/" + carImage);
        float scaler = ((document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin() - 0) / img.getWidth()) * 100;

        img.scalePercent(scaler);
        document.add(img);


        document.close();
        return "ok";
    }

    static public ArrayList<Airbag> createRanndomAirbag() {

      String[] descriptions = {"kierowca", "pasazer", "kanapa", "boczne"};
      boolean[] state = {false, true};
      Random rand = new Random();
      ArrayList<Airbag> newAirbag = new ArrayList<>();
      for (int i = 0; i < 4; i++){
          int s = rand.nextInt(2);
          newAirbag.add(new Airbag(descriptions[i], state[s]));
      }
      return newAirbag;
    };



    static String generateCars() {
        String[] models = { "Renault", "Audi", "Opel" };
        String[] colors = { "#ff0000", "#dc143a", "#889ac4" };
        Integer[] years = { 2000, 2001, 2002, 2003, 2004, 2005, 2006};
        int[] vaty = { 0, 7, 20 };

        Random rand = new Random();

        //cars.clear();
        int currId = cars.size();

        for (int i = 0; i < 10; i++) {
            int m = rand.nextInt(3);
            int c = rand.nextInt(3);
            int y = rand.nextInt(7);
            int v = rand.nextInt(3);
            ArrayList<Airbag> randomAirbag = createRanndomAirbag();
            //new ArrayList<Airbag>();
            UUID uuid = Generators.randomBasedGenerator().generate();
            Car car = new Car(currId++, years[y], models[m], colors[c], (int)Math.floor(Math.random()*185000)+15000, randomAirbag, models[m] + ".png", vaty[v]);
            car.setUUID(uuid);
            cars.add(car);
        }

        return "ok";
    }

    static String getCarById(Request req) {
        Gson gson = new Gson();
        UUID uuid = gson.fromJson(req.body(), Car.class).getUUID();
        for (Car c : cars) {
            if (c.getUUID().equals(uuid))
                return gson.toJson(c);
        }

        return "nie";
    }

    static String deleteById(Request req) {
        Gson gson = new Gson();
        UUID uuid = gson.fromJson(req.body(), Car.class).getUUID();
        cars.removeIf(car -> car.getUUID().equals(uuid));

        return "Car deleted";
    }

    static String editCar(Request req) {
        Gson gson = new Gson();
        EditCarRequest editCarObj = gson.fromJson(req.body(), EditCarRequest.class);

        for (Car c : cars) {
            if (c.getUUID().equals(editCarObj.getUUID())) {
                c.setModel(editCarObj.getModel());
                c.setYear(editCarObj.getYear());
            }
        }

        return "Car edited";
    }

    static String displayJSONData(Response res) {
        Type listType = new TypeToken<ArrayList<Car>>() {}.getType();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        res.type("application/json");
        return gson.toJson(cars, listType);
    }

    static String addNewCar(Request req, Response res) {
        String[] models = { "Renault", "Audi", "Opel" };
        int[] vaty = { 0, 7, 20 };
        Random rand = new Random();
        int m = rand.nextInt(3);
        int v = rand.nextInt(3);
        Gson gson = new Gson();
        int newId = cars.size();
        Car car = gson.fromJson(req.body(), Car.class);
            car.setUUID(Generators.randomBasedGenerator().generate());
            car.setId(newId++);
            car.setImage(models[m] + ".png");
            car.setVat(vaty[v]);
            car.getYear();
            car.setFirstCustomDate();
        cars.add(car);
        //System.out.println(car);
        //System.out.println(cars);
        res.type("text/plain");
        return "Dodano nowy samochód";
    }
}
