package org.pa.balance.client.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Board
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-04-08T17:12:10.402735-04:00[America/New_York]")

public class Board   {
  @JsonProperty("id")
  private Long id;

  @JsonProperty("year")
  private Integer year;

  @JsonProperty("month")
  private Integer month;

  @JsonProperty("startAmt")
  private BigDecimal startAmt;

  @JsonProperty("account")
  private String account;

  public Board id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  */
  @ApiModelProperty(value = "")


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Board year(Integer year) {
    this.year = year;
    return this;
  }

  /**
   * Get year
   * @return year
  */
  @ApiModelProperty(value = "")


  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public Board month(Integer month) {
    this.month = month;
    return this;
  }

  /**
   * Get month
   * @return month
  */
  @ApiModelProperty(value = "")


  public Integer getMonth() {
    return month;
  }

  public void setMonth(Integer month) {
    this.month = month;
  }

  public Board startAmt(BigDecimal startAmt) {
    this.startAmt = startAmt;
    return this;
  }

  /**
   * Get startAmt
   * @return startAmt
  */
  @ApiModelProperty(value = "")

  @Valid

  public BigDecimal getStartAmt() {
    return startAmt;
  }

  public void setStartAmt(BigDecimal startAmt) {
    this.startAmt = startAmt;
  }

  public Board account(String account) {
    this.account = account;
    return this;
  }

  /**
   * Get account
   * @return account
  */
  @ApiModelProperty(value = "")


  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Board board = (Board) o;
    return Objects.equals(this.id, board.id) &&
        Objects.equals(this.year, board.year) &&
        Objects.equals(this.month, board.month) &&
        Objects.equals(this.startAmt, board.startAmt) &&
        Objects.equals(this.account, board.account);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, year, month, startAmt, account);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Board {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    year: ").append(toIndentedString(year)).append("\n");
    sb.append("    month: ").append(toIndentedString(month)).append("\n");
    sb.append("    startAmt: ").append(toIndentedString(startAmt)).append("\n");
    sb.append("    account: ").append(toIndentedString(account)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

