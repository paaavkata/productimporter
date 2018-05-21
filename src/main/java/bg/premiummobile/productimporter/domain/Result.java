package bg.premiummobile.productimporter.domain;

public class Result {

	private int sequenceNumber;
	private String id;
	private String name;
	private int totalTime;
	private int successfullUploadedPhotos;
	private int photos;
	private int magentoUploadStatus;
	public int getSequenceNumber() {
		return sequenceNumber;
	}
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}
	public int getSuccessfullUploadedPhotos() {
		return successfullUploadedPhotos;
	}
	public void setSuccessfullUploadedPhotos(int successfullUploadedPhotos) {
		this.successfullUploadedPhotos = successfullUploadedPhotos;
	}
	public int getPhotos() {
		return photos;
	}
	public void setPhotos(int photos) {
		this.photos = photos;
	}
	public int getMagentoUploadStatus() {
		return magentoUploadStatus;
	}
	public void setMagentoUploadStatus(int magentoUploadStatus) {
		this.magentoUploadStatus = magentoUploadStatus;
	}
}
