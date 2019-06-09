package rostem.utils.mapper;

import rostem.model.dto.response.ResponseCategory;
import rostem.model.dto.request.RequestCategory;
import rostem.model.entities.Category;

public class CategoryMapper {

    public static Category map(RequestCategory requestCategory){
        Category category = new Category();
        category.setName(requestCategory.getName());
        category.setDescription(requestCategory.getDescription());

        return category;
    }

    public static ResponseCategory map(Category category){
        ResponseCategory responseCategory = new ResponseCategory();
        responseCategory.setId(category.getId());
        responseCategory.setName(category.getName());
        responseCategory.setDescription(category.getDescription());

        return responseCategory;
    }
}
