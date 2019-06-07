package br.com.finperson.model.jsonserializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import br.com.finperson.model.CategoryEntity;

public class CategoryEntitySerializer extends StdSerializer<CategoryEntity> {

	private static final long serialVersionUID = 1L;

	public CategoryEntitySerializer() {
        this(null);
    }
 
    public CategoryEntitySerializer(Class<CategoryEntity> t) {
        super(t);
    }

	@Override
	public void serialize(CategoryEntity value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		
		CategoryEntity category = new CategoryEntity();
		category.setId(value.getId());
		category.setColor(value.getColor());
		category.setIcon(value.getIcon());
		category.setActive(value.getActive());
		category.setName(value.getName());
		category.setUser(value.getUser());
		
		gen.writeObject(category);
		
	}

}
