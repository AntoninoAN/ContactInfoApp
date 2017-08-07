package com.example.admin.contactinfoapp.Data;



import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Admin on 8/6/2017.
 */

public final class ParseJsontoData {
    public ParseJsontoData(){}

    public  Result setFromJSONtoPOJO(JSONObject object) throws JSONException{
        Result itemResult= new Result();
        itemResult.setName(helperParserName(object.getJSONObject(Result.Titles.name.toString())));
        itemResult.setLocation(helperParserLocation(object.getJSONObject(Result.Titles.location.toString())));
        itemResult.setEmail(object.getString(Result.Titles.email.toString()));
        itemResult.setPicture(helperParserPicture(object.getJSONObject(Result.Titles.picture.toString())));
        itemResult.setGender(object.getString(Result.Titles.gender.toString()));
        return itemResult;
    }
    public  Name helperParserName(JSONObject help){
        Name nameinst = new Name();
        try {

            nameinst.setFirst(help.getString(Name.Titles.first.toString()));
            nameinst.setLast(help.getString(Name.Titles.last.toString()));
            nameinst.setTitle(help.getString(Name.Titles.title.toString()));
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return nameinst;
    }
    public  Location helperParserLocation(JSONObject help){
        Location location=new Location();
        try {
            location.setStreet(help.getString(Location.Titles.street.toString()));
            location.setCity(help.getString(Location.Titles.city.toString()));
            location.setState(help.getString(Location.Titles.state.toString()));

        }catch (JSONException e){
            e.printStackTrace();
        }
        return location;
    }
    public  Picture helperParserPicture(JSONObject help){
        Picture picture= new Picture();
        try{
            picture.setLarge(help.getString(Picture.Title.large.toString()));

        }catch (JSONException e){
            e.printStackTrace();
        }
        return picture;
    }
}
