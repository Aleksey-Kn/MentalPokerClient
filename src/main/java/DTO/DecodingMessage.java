package DTO;

public class DecodingMessage {
    private int message;
    private int ownerID;

    public DecodingMessage(){}

    public DecodingMessage(int id, int message){
        ownerID = id;
        this.message = message;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public int getMessage() {
        return message;
    }
}
