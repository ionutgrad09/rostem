package rostem.utils.mapper;

import java.util.Date;
import rostem.model.dto.request.RequestMessage;
import rostem.model.dto.response.ResponseMessage;
import rostem.model.entities.Message;

public class MessageMapper {

    public static Message map(RequestMessage requestMessage) {
        Message message = new Message();
        message.setFrom(requestMessage.getFrom());
        message.setTo(requestMessage.getTo());
        message.setMessage(requestMessage.getMessage());
        message.setCreationDate(new Date());

        return message;
    }

    public static ResponseMessage map(Message message) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setCreationDate(message.getCreationDate().toString());
        responseMessage.setFrom(message.getFrom());
        responseMessage.setTo(message.getTo());
        responseMessage.setId(message.getId());
        responseMessage.setMessage(message.getMessage());

        return responseMessage;
    }
}
