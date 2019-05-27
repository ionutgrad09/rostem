package rostem.utils.mapper;

import rostem.model.dto.request.RequestChapter;
import rostem.model.dto.response.ResponseChapter;
import rostem.model.material.Chapter;

public class ChapterMapper {

    public static Chapter map(RequestChapter requestChapter) {
        Chapter chapter = new Chapter();
        chapter.setName(requestChapter.getName());
        chapter.setDescription(requestChapter.getDescription());

        return chapter;
    }

    public static ResponseChapter map(Chapter chapter) {
        ResponseChapter responseChapter = new ResponseChapter();
        responseChapter.setId(chapter.getId());
        responseChapter.setName(chapter.getName());
        responseChapter.setDescription(chapter.getDescription());
        responseChapter.setTutorialName(chapter.getTutorial().getName());

        return responseChapter;
    }

}
