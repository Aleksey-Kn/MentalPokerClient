package DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DecodingMessage {
    private int message;
    private int ownerID;

    public DecodingMessage(int id, int message){
        ownerID = id;
        this.message = message;
    }
}
