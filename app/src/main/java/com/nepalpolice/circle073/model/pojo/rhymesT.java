/*
 * Copyright (c) 2015-2016 Filippo Engidashet. All Rights Reserved.
 * <p>
 *  Save to the extent permitted by law, you may not use, copy, modify,
 *  distribute or create derivative works of this material or any part
 *  of it without the prior written consent of Filippo Engidashet.
 *  <p>
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 */

package com.nepalpolice.circle073.model.pojo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.annotations.Expose;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author Filippo Engidashet
 * @version 1.0.0
 * @date 1/22/2016
 */
public class rhymesT implements Serializable {

    private static final long serialVersionUID = 111696345129311948L;
    public byte[] imageByteArray;

    @Expose
    private String category;

    @Expose
    private String price;

    @Expose
    private String instructions;

    @Expose
    private String photo;

    @Expose
    private String name;

    @Expose
    private String productId;

    @Expose
    private String duration;

    private Bitmap picture;

    private boolean isFromDatabase;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String category) {
        this.duration = duration;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public boolean isFromDatabase() {
        return isFromDatabase;
    }

    public void setFromDatabase(boolean fromDatabase) {
        isFromDatabase = fromDatabase;
    }


    private void writeObject(java.io.ObjectOutputStream out) throws IOException {

        out.writeObject(category);
        out.writeObject(price);
        out.writeObject(instructions);
        out.writeObject(photo);
        out.writeObject(name);
        out.writeObject(productId);
        out.writeObject(duration);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.PNG, 0, byteStream);
        byte bitmapBytes[] = byteStream.toByteArray();
        out.write(bitmapBytes, 0, bitmapBytes.length);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {

        category = (String) in.readObject();
        price = (String) in.readObject();
        instructions = (String) in.readObject();
        photo = (String) in.readObject();
        name = (String) in.readObject();
        productId = (String) in.readObject();
        duration = (String) in.readObject();

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int b;
        while ((b = in.read()) != -1)
            byteStream.write(b);
        byte bitmapBytes[] = byteStream.toByteArray();
        picture = BitmapFactory.decodeByteArray(bitmapBytes, 0,
                bitmapBytes.length);
    }
}
