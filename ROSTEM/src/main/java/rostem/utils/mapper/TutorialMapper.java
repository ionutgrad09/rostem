package rostem.utils.mapper;

import rostem.model.dto.request.RequestTutorial;
import rostem.model.dto.response.ResponseTutorial;
import rostem.model.entities.Tutorial;

public class TutorialMapper {

    public static Tutorial map(RequestTutorial requestTutorial){
        Tutorial tutorial = new Tutorial();
        tutorial.setName(requestTutorial.getName());
        tutorial.setDescription(requestTutorial.getDescription());

        return tutorial;
    }

    public static ResponseTutorial map(Tutorial tutorial){
        ResponseTutorial responseTutorial = new ResponseTutorial();
        responseTutorial.setId(tutorial.getId());
        responseTutorial.setName(tutorial.getName());
        responseTutorial.setDescription(tutorial.getDescription());
        responseTutorial.setCategoryName(tutorial.getCategory().getName());

        return responseTutorial;
    }
}
