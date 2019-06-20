package rostem.utils.mapper;

import java.util.Date;
import rostem.model.dto.request.RequestChapter;
import rostem.model.dto.response.ResponseChapter;
import rostem.model.entities.Chapter;

public class ChapterMapper {

    public static Chapter map(RequestChapter requestChapter) {
        Chapter chapter = new Chapter();
        chapter.setName(requestChapter.getName());
        chapter.setContent(requestChapter.getContent());
        chapter.setCreationDate(new Date());

        return chapter;
    }

    public static ResponseChapter map(Chapter chapter) {
        ResponseChapter responseChapter = new ResponseChapter();
        responseChapter.setId(chapter.getId());
        responseChapter.setName(chapter.getName());
        responseChapter.setContent(chapter.getContent());
        responseChapter.setTutorialName(chapter.getTutorial().getName());
        responseChapter.setCreationDate(chapter.getCreationDate().toString());

        return responseChapter;
    }

}
