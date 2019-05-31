package rostem.service.material;

import static rostem.utils.ApiResponses.CHAPTER_ALREADY_EXISTS;
import static rostem.utils.ApiResponses.CHAPTER_NOT_FOUND;
import static rostem.utils.ApiResponses.TUTORIAL_NOT_FOUND;
import static rostem.utils.mapper.ChapterMapper.map;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rostem.model.dto.request.RequestChapter;
import rostem.model.dto.response.ResponseChapter;
import rostem.model.material.Chapter;
import rostem.repository.materials.ChapterRepository;
import rostem.repository.materials.TutorialRepository;
import rostem.utils.exception.RostemException;
import rostem.utils.mapper.ChapterMapper;

@Service
public class ChapterService {

    private final static Logger logger = LoggerFactory.getLogger(ChapterService.class);

    private TutorialRepository tutorialRepository;
    private ChapterRepository chapterRepository;

    @Autowired
    public ChapterService(TutorialRepository tutorialRepository, ChapterRepository chapterRepository) {
        this.tutorialRepository = tutorialRepository;
        this.chapterRepository = chapterRepository;
    }

    public List<ResponseChapter> getAllChapters() {
        logger.info("[CHAPTER] Get all chapters.");

        return chapterRepository.findAll().stream().map(ChapterMapper::map).collect(Collectors.toList());
    }

    public List<ResponseChapter> getChaptersForTutorial(Long id) {
        logger.info("[CHAPTER] Trying to get all chapters for tutorial " + id);

        if (!findTutorialById(id)) {
            throw new RostemException(TUTORIAL_NOT_FOUND);
        } else {
            return tutorialRepository.findTutorialById(id).get().getChapters().stream()
                    .map(ChapterMapper::map)
                    .collect(Collectors.toList());
        }
    }

    public ResponseChapter createChapter(RequestChapter requestChapter) {
        final Long tutorialId = requestChapter.getTutorialId();
        final String name = requestChapter.getName();
        logger.info("[CHAPTER] Trying to add a new chapter " + name);

        if (!findTutorialById(tutorialId)) {
            throw new RostemException(TUTORIAL_NOT_FOUND);
        } else {
            if (findChapterByName(name)) {
                throw new RostemException(CHAPTER_ALREADY_EXISTS);
            } else {
                Chapter chapter = map(requestChapter);
                chapter.setTutorial(tutorialRepository.findTutorialById(tutorialId).get());
                return map(chapterRepository.save(chapter));
            }
        }
    }

    @Transactional
    public void deleteChapters(List<Long> ids) {
        for (Long id : ids) {
            logger.info("[CHAPTER] Trying to delete chapter " + id);

            if (!findChapterById(id)) {
                throw new RostemException(CHAPTER_NOT_FOUND);
            } else {
                chapterRepository.deleteById(id);
            }
        }
    }

    @Transactional
    public ResponseChapter updateChapter(Long id, RequestChapter requestChapter) {
        logger.info("[CHAPTER] Trying to update chapter " + id);

        if (!findChapterById(id)) {
            throw new RostemException(CHAPTER_NOT_FOUND);
        } else {
            Chapter chapter = map(requestChapter);
            chapter.setId(id);
            chapter.setTutorial(tutorialRepository.findTutorialById(requestChapter.getTutorialId()).get());
            return map(chapterRepository.save(chapter));
        }
    }

    public List<ResponseChapter> getLatestChapters(int count) {
        logger.info("[CHAPTER] Getting latest created chapters..");

        return chapterRepository.findAllByOrderByCreationDateDesc().stream().limit(count).map(ChapterMapper::map)
                .collect(Collectors.toList());
    }


    private boolean findTutorialById(Long id) {
        return tutorialRepository.findTutorialById(id).isPresent();
    }

    private boolean findChapterById(Long id) {
        return chapterRepository.findChapterById(id).isPresent();
    }

    private boolean findChapterByName(String name) {
        return chapterRepository.findChapterByName(name).isPresent();
    }
}
