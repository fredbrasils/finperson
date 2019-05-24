package br.com.finperson.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.finperson.model.jsonserializer.CategoryEntitySerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(callSuper=true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
public class CategoryEntity extends BaseEntity{

	private static final long serialVersionUID = 1L;
	
	@Builder
	public CategoryEntity(Long id, String name, String color, String icon, UserEntity user, 
			CategoryEntity mainCategory, Set<CategoryEntity> subCategories, Boolean active) {
        super(id);     
        this.name = name;
        this.color = color;
        this.icon = icon;
        this.user = user;
        this.mainCategory = mainCategory;
        this.subCategories = subCategories;
        this.active = active;
    }
	
	@JsonSerialize(using = CategoryEntitySerializer.class)
	@ManyToOne
	@JoinColumn(name = "main_category_id")
	CategoryEntity mainCategory;
		
	@OneToMany(mappedBy="mainCategory")
	Set<CategoryEntity> subCategories;
	
	@NotEmpty
	@NotNull
	@Column
	private String color;
	
	@NotEmpty
	@NotNull
	@Column
	private String icon;
	
	@NotEmpty
	@NotNull
	@Column
	private String name;	
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;	
	
	@Builder.Default
	@Column
    private Boolean active = true;
}
