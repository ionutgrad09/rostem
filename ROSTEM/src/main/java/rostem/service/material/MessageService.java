package rostem.service.material;

import static rostem.utils.mapper.MessageMapper.map;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rostem.model.dto.request.RequestMessage;
import rostem.model.dto.response.ResponseMessage;
import rostem.repository.materials.MessageRepository;
import rostem.utils.mapper.MessageMapper;

@Service
public class MessageService {

    private final static Logger logger = LoggerFactory.getLogger(MessageService.class);

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Transactional
    public List<ResponseMessage> getMessagesSent(String email) {
        logger.info("[MESSAGE] Getting all messages sent by " + email);
        return messageRepository.findAllBySentByOrderByCreationDateDesc(email).stream().map(MessageMapper::map)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ResponseMessage> getMessagesReceived(String email) {
        logger.info("[MESSAGE] Getting all messages received by " + email);
        return messageRepository.findAllByReceivedByOrderByCreationDateDesc(email).stream().map(MessageMapper::map)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addMessage(RequestMessage requestMessage) {
        logger.info("[MESSAGE] Trying to add a new message.");
        messageRepository.save(map(requestMessage));
    }
}
