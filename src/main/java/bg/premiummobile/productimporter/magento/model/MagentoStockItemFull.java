package bg.premiummobile.productimporter.magento.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class MagentoStockItemFull{
	
	@JsonProperty("stock_id")
	private Integer stockId;
	@JsonProperty("qty")
	private Integer qty;
	@JsonProperty("is_in_stock")
	private boolean isInStock;
	@JsonProperty("item_id")
	private Integer itemId;
	@JsonProperty("product_id")
	private int productId;
	@JsonProperty("is_qty_decimal")
	private boolean isQtyDecimal;
	@JsonProperty("show_default_notification_message")
	private boolean showDefualtNotificationMessage;
	@JsonProperty("use_config_min_qty")
	private boolean useConfigMinQty;
	@JsonProperty("min_qty")
	private int minQty;
	@JsonProperty("use_config_min_sale_qty")
	private boolean useConfigMinSaleQty;
	@JsonProperty("min_sale_qty")
	private int minSaleQty;
	@JsonProperty("use_config_max_sale_qty")
	private boolean useConfigMaxSaleQty;
	@JsonProperty("max_sale_qty")
	private int maxSaleQty;
	@JsonProperty("use_config_backorders")
	private boolean useConfigBackorders;
	@JsonProperty("backorders")
	private int backorders;
	@JsonProperty("use_config_notify_stock_qty")
	private boolean useConfigNotifyStockQty;
	@JsonProperty("notify_stock_qty")
	private int notifyStockQty;
	@JsonProperty("use_config_qty_increments")
	private boolean useConfigQtyIncrements;
	@JsonProperty("qty_increments")
	private int qtyIncrements;
	@JsonProperty("use_config_enable_qty_inc")
	private boolean useConfigEnableQtyInc;
	@JsonProperty("enable_qty_increments")
	private boolean enableQtyIncrements;
	@JsonProperty("use_config_manage_stock")
	private boolean useConfigManageStock;
	@JsonProperty("manage_stock")
	private boolean manageStock;
	@JsonProperty("low_stock_date")
	private String lowStockDate;
	@JsonProperty("is_decimal_divided")
	private boolean decimalDevided;
	@JsonProperty("stock_status_changed_auto")
	private int stockStatusChangedAuto;
	
	public int getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	@JsonIgnore
	public boolean isQtyDecimal() {
		return isQtyDecimal;
	}
	@JsonIgnore
	public void setQtyDecimal(boolean isQtyDecimal) {
		this.isQtyDecimal = isQtyDecimal;
	}
	public boolean isShowDefualtNotificationMessage() {
		return showDefualtNotificationMessage;
	}
	public void setShowDefualtNotificationMessage(boolean showDefualtNotificationMessage) {
		this.showDefualtNotificationMessage = showDefualtNotificationMessage;
	}
	public boolean isUseConfigMinQty() {
		return useConfigMinQty;
	}
	public void setUseConfigMinQty(boolean useConfigMinQty) {
		this.useConfigMinQty = useConfigMinQty;
	}
	public int getMinQty() {
		return minQty;
	}
	public void setMinQty(int minQty) {
		this.minQty = minQty;
	}
	public boolean isUseConfigMinSaleQty() {
		return useConfigMinSaleQty;
	}
	public void setUseConfigMinSaleQty(boolean useConfigMinSaleQty) {
		this.useConfigMinSaleQty = useConfigMinSaleQty;
	}
	public int getMinSaleQty() {
		return minSaleQty;
	}
	public void setMinSaleQty(int minSaleQty) {
		this.minSaleQty = minSaleQty;
	}
	public boolean isUseConfigMaxSaleQty() {
		return useConfigMaxSaleQty;
	}
	public void setUseConfigMaxSaleQty(boolean useConfigMaxSaleQty) {
		this.useConfigMaxSaleQty = useConfigMaxSaleQty;
	}
	public int getMaxSaleQty() {
		return maxSaleQty;
	}
	public void setMaxSaleQty(int maxSaleQty) {
		this.maxSaleQty = maxSaleQty;
	}
	public boolean isUseConfigBackorders() {
		return useConfigBackorders;
	}
	public void setUseConfigBackorders(boolean useConfigBackorders) {
		this.useConfigBackorders = useConfigBackorders;
	}
	public int getBackorders() {
		return backorders;
	}
	public void setBackorders(int backorders) {
		this.backorders = backorders;
	}
	public boolean isUseConfigNotifyStockQty() {
		return useConfigNotifyStockQty;
	}
	public void setUseConfigNotifyStockQty(boolean useConfigNotifyStockQty) {
		this.useConfigNotifyStockQty = useConfigNotifyStockQty;
	}
	public int getNotifyStockQty() {
		return notifyStockQty;
	}
	public void setNotifyStockQty(int notifyStockQty) {
		this.notifyStockQty = notifyStockQty;
	}
	public boolean isUseConfigQtyIncrements() {
		return useConfigQtyIncrements;
	}
	public void setUseConfigQtyIncrements(boolean useConfigQtyIncrements) {
		this.useConfigQtyIncrements = useConfigQtyIncrements;
	}
	public int getQtyIncrements() {
		return qtyIncrements;
	}
	public void setQtyIncrements(int qtyIncrements) {
		this.qtyIncrements = qtyIncrements;
	}
	public boolean isUseConfigEnableQtyInc() {
		return useConfigEnableQtyInc;
	}
	public void setUseConfigEnableQtyInc(boolean useConfigEnableQtyInc) {
		this.useConfigEnableQtyInc = useConfigEnableQtyInc;
	}
	public boolean isEnableQtyIncrements() {
		return enableQtyIncrements;
	}
	public void setEnableQtyIncrements(boolean enableQtyIncrements) {
		this.enableQtyIncrements = enableQtyIncrements;
	}
	public boolean isUseConfigManageStock() {
		return useConfigManageStock;
	}
	public void setUseConfigManageStock(boolean useConfigManageStock) {
		this.useConfigManageStock = useConfigManageStock;
	}
	public boolean isManageStock() {
		return manageStock;
	}
	public void setManageStock(boolean manageStock) {
		this.manageStock = manageStock;
	}
	public String getLowStockDate() {
		return lowStockDate;
	}
	public void setLowStockDate(String lowStockDate) {
		this.lowStockDate = lowStockDate;
	}
	public boolean isDecimalDevided() {
		return decimalDevided;
	}
	public void setDecimalDevided(boolean decimalDevided) {
		this.decimalDevided = decimalDevided;
	}
	public int getStockStatusChangedAuto() {
		return stockStatusChangedAuto;
	}
	public void setStockStatusChangedAuto(int stockStatusChangedAuto) {
		this.stockStatusChangedAuto = stockStatusChangedAuto;
	}

	public Integer getStockId() {
		return stockId;
	}
	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}
	public Integer getQty() {
		if(this.qty == null){
			return 0;
		}
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	@JsonIgnore
	public boolean isInStock() {
		return isInStock;
	}
	@JsonIgnore
	public void setInStock(boolean isInStock) {
		this.isInStock = isInStock;
	}
}
