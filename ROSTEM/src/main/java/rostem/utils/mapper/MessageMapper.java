package rostem.utils.mapper;

import java.util.Date;
import rostem.model.dto.request.RequestMessage;
import rostem.model.dto.response.ResponseMessage;
import rostem.model.entities.Message;

public class MessageMapper {

    public static Message map(RequestMessage requestMessage) {
        Message message = new Message();
        message.setSentBy(requestMessage.getSentBy());
        message.setReceivedBy(requestMessage.getReceivedBy());
        message.setMessage(requestMessage.getMessage());
        message.setCreationDate(new Date());

        return message;
    }

    public static ResponseMessage map(Message message) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setCreationDate(message.getCreationDate().toString());
        responseMessage.setSentBy(message.getSentBy());
        responseMessage.setReceivedBy(message.getReceivedBy());
        responseMessage.setId(message.getId());
        responseMessage.setMessage(message.getMessage());

        return responseMessage;
    }
}
