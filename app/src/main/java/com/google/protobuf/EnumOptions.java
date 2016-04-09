// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: google/protobuf/descriptor.proto at 530:1
package com.google.protobuf;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;
import okio.ByteString;

public final class EnumOptions extends Message<EnumOptions, EnumOptions.Builder> {
  public static final ProtoAdapter<EnumOptions> ADAPTER = new ProtoAdapter_EnumOptions();

  private static final long serialVersionUID = 0L;

  public static final Boolean DEFAULT_ALLOW_ALIAS = false;

  public static final Boolean DEFAULT_DEPRECATED = false;

  /**
   * Set this option to true to allow mapping different tag names to the same
   * value.
   */
  @WireField(
      tag = 2,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean allow_alias;

  /**
   * Is this enum deprecated?
   * Depending on the target platform, this can emit Deprecated annotations
   * for the enum, or it will be completely ignored; in the very least, this
   * is a formalization for deprecating enums.
   */
  @WireField(
      tag = 3,
      adapter = "com.squareup.wire.ProtoAdapter#BOOL"
  )
  public final Boolean deprecated;

  /**
   * The parser stores options it doesn't recognize here. See above.
   */
  @WireField(
      tag = 999,
      adapter = "com.google.protobuf.UninterpretedOption#ADAPTER",
      label = WireField.Label.REPEATED
  )
  public final List<UninterpretedOption> uninterpreted_option;

  public EnumOptions(Boolean allow_alias, Boolean deprecated, List<UninterpretedOption> uninterpreted_option) {
    this(allow_alias, deprecated, uninterpreted_option, ByteString.EMPTY);
  }

  public EnumOptions(Boolean allow_alias, Boolean deprecated, List<UninterpretedOption> uninterpreted_option, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.allow_alias = allow_alias;
    this.deprecated = deprecated;
    this.uninterpreted_option = Internal.immutableCopyOf("uninterpreted_option", uninterpreted_option);
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.allow_alias = allow_alias;
    builder.deprecated = deprecated;
    builder.uninterpreted_option = Internal.copyOf("uninterpreted_option", uninterpreted_option);
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof EnumOptions)) return false;
    EnumOptions o = (EnumOptions) other;
    return Internal.equals(unknownFields(), o.unknownFields())
        && Internal.equals(allow_alias, o.allow_alias)
        && Internal.equals(deprecated, o.deprecated)
        && Internal.equals(uninterpreted_option, o.uninterpreted_option);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (allow_alias != null ? allow_alias.hashCode() : 0);
      result = result * 37 + (deprecated != null ? deprecated.hashCode() : 0);
      result = result * 37 + (uninterpreted_option != null ? uninterpreted_option.hashCode() : 1);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (allow_alias != null) builder.append(", allow_alias=").append(allow_alias);
    if (deprecated != null) builder.append(", deprecated=").append(deprecated);
    if (uninterpreted_option != null) builder.append(", uninterpreted_option=").append(uninterpreted_option);
    return builder.replace(0, 2, "EnumOptions{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<EnumOptions, Builder> {
    public Boolean allow_alias;

    public Boolean deprecated;

    public List<UninterpretedOption> uninterpreted_option;

    public Builder() {
      uninterpreted_option = Internal.newMutableList();
    }

    /**
     * Set this option to true to allow mapping different tag names to the same
     * value.
     */
    public Builder allow_alias(Boolean allow_alias) {
      this.allow_alias = allow_alias;
      return this;
    }

    /**
     * Is this enum deprecated?
     * Depending on the target platform, this can emit Deprecated annotations
     * for the enum, or it will be completely ignored; in the very least, this
     * is a formalization for deprecating enums.
     */
    public Builder deprecated(Boolean deprecated) {
      this.deprecated = deprecated;
      return this;
    }

    /**
     * The parser stores options it doesn't recognize here. See above.
     */
    public Builder uninterpreted_option(List<UninterpretedOption> uninterpreted_option) {
      Internal.checkElementsNotNull(uninterpreted_option);
      this.uninterpreted_option = uninterpreted_option;
      return this;
    }

    @Override
    public EnumOptions build() {
      return new EnumOptions(allow_alias, deprecated, uninterpreted_option, buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_EnumOptions extends ProtoAdapter<EnumOptions> {
    ProtoAdapter_EnumOptions() {
      super(FieldEncoding.LENGTH_DELIMITED, EnumOptions.class);
    }

    @Override
    public int encodedSize(EnumOptions value) {
      return (value.allow_alias != null ? ProtoAdapter.BOOL.encodedSizeWithTag(2, value.allow_alias) : 0)
          + (value.deprecated != null ? ProtoAdapter.BOOL.encodedSizeWithTag(3, value.deprecated) : 0)
          + UninterpretedOption.ADAPTER.asRepeated().encodedSizeWithTag(999, value.uninterpreted_option)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, EnumOptions value) throws IOException {
      if (value.allow_alias != null) ProtoAdapter.BOOL.encodeWithTag(writer, 2, value.allow_alias);
      if (value.deprecated != null) ProtoAdapter.BOOL.encodeWithTag(writer, 3, value.deprecated);
      if (value.uninterpreted_option != null) UninterpretedOption.ADAPTER.asRepeated().encodeWithTag(writer, 999, value.uninterpreted_option);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public EnumOptions decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 2: builder.allow_alias(ProtoAdapter.BOOL.decode(reader)); break;
          case 3: builder.deprecated(ProtoAdapter.BOOL.decode(reader)); break;
          case 999: builder.uninterpreted_option.add(UninterpretedOption.ADAPTER.decode(reader)); break;
          default: {
            FieldEncoding fieldEncoding = reader.peekFieldEncoding();
            Object value = fieldEncoding.rawProtoAdapter().decode(reader);
            builder.addUnknownField(tag, fieldEncoding, value);
          }
        }
      }
      reader.endMessage(token);
      return builder.build();
    }

    @Override
    public EnumOptions redact(EnumOptions value) {
      Builder builder = value.newBuilder();
      Internal.redactElements(builder.uninterpreted_option, UninterpretedOption.ADAPTER);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
