package bg.premiummobile.productimporter.magento.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MediaGalleryEntry {
	
	@JsonIgnore
	private int id;
	@JsonProperty("media_type")
	private String mediaType;
	@JsonProperty("label")
	private String label;
	@JsonProperty("position")
	private int position;
	@JsonProperty("disabled")
	private boolean disabled;
	@JsonProperty("types")
	private List<String> types;
	@JsonProperty("file")
	private String fileName;
	@JsonProperty("content")
	private MediaGalleryContent content;

	public MediaGalleryContent getContent() {
		return content;
	}

	public void setContent(MediaGalleryContent content) {
		this.content = content;
	}
	@JsonIgnore
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
