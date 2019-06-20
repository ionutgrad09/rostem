package rostem.service.material;

import static rostem.utils.ApiResponses.CATEGORY_NOT_FOUND;
import static rostem.utils.ApiResponses.TUTORIAL_ALREADY_EXISTS;
import static rostem.utils.ApiResponses.TUTORIAL_NOT_FOUND;
import static rostem.utils.mapper.TutorialMapper.map;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rostem.model.dto.request.RequestTutorial;
import rostem.model.dto.response.ResponseTutorial;
import rostem.model.entities.Tutorial;
import rostem.repository.materials.CategoryRepository;
import rostem.repository.materials.TutorialRepository;
import rostem.utils.exception.RostemException;
import rostem.utils.mapper.TutorialMapper;

@Service
public class TutorialService {

    private final static Logger logger = LoggerFactory.getLogger(TutorialService.class);

    private TutorialRepository tutorialRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public TutorialService(TutorialRepository tutorialRepository, CategoryRepository categoryRepository) {
        this.tutorialRepository = tutorialRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public List<ResponseTutorial> getAllTutorials() {
        logger.info("[TUTORIAL] Get all tutorial");

        return tutorialRepository.findAll().stream().map(TutorialMapper::map).collect(Collectors.toList());
    }

    @Transactional
    public List<ResponseTutorial> getTutorialsForCategory(String categoryName) {
        logger.info("[TUTORIAL] Trying to get all tutorials for category " + categoryName);

        if (!findCategoryByName(categoryName)) {
            throw new RostemException(CATEGORY_NOT_FOUND);
        } else {
            return categoryRepository.findCategoryByName(categoryName).getTutorials().stream()
                    .map(TutorialMapper::map)
                    .collect(Collectors.toList());
        }
    }

    public ResponseTutorial createTutorial(RequestTutorial requestTutorial) {
        final Long categoryId = requestTutorial.getCategoryId();
        final String name = requestTutorial.getName();
        logger.info("[TUTORIAL] Trying to add a new tutorial " + name);

        if (!findCategoryById(categoryId)) {
            throw new RostemException(CATEGORY_NOT_FOUND);
        } else {
            if (findTutorialByName(name)) {
                throw new RostemException(TUTORIAL_ALREADY_EXISTS);
            } else {
                Tutorial tutorial = map(requestTutorial);
                tutorial.setCategory(categoryRepository.findCategoryById(categoryId));
                return map(tutorialRepository.save(tutorial));
            }
        }
    }

    @Transactional
    public void deleteTutorials(List<Long> ids) {
        for (Long id : ids) {
            logger.info("[TUTORIAL] Trying to delete tutorial " + id);

            if (!findTutorialById(id)) {
                throw new RostemException(TUTORIAL_NOT_FOUND);
            } else {
                tutorialRepository.deleteById(id);
            }
        }
    }

    @Transactional
    public ResponseTutorial updateTutorial(Long id, RequestTutorial requestTutorial) {
        logger.info("[TUTORIAL] Trying to update tutorial " + id);

        if (!findTutorialById(id)) {
            throw new RostemException(TUTORIAL_NOT_FOUND);
        } else {
            Tutorial tutorial = map(requestTutorial);
            tutorial.setId(id);
            tutorial.setCategory(categoryRepository.findCategoryById(requestTutorial.getCategoryId()));
            return map(tutorialRepository.save(tutorial));
        }
    }

    private boolean findCategoryByName(String categoryName) {
        return categoryRepository.findCategoryByName(categoryName) != null;
    }

    private boolean findCategoryById(Long id) {
        return categoryRepository.findCategoryById(id) != null;
    }

    private boolean findTutorialById(Long id) {
        return tutorialRepository.findTutorialById(id) != null;
    }

    private boolean findTutorialByName(String name) {
        return tutorialRepository.findTutorialByName(name) != null;
    }
}
