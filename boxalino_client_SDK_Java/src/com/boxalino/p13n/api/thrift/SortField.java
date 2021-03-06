/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.boxalino.p13n.api.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
/**
 * field to be used for sorting
 */
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.10.0)", date = "2017-01-25")
public class SortField implements org.apache.thrift.TBase<SortField, SortField._Fields>, java.io.Serializable, Cloneable, Comparable<SortField> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("SortField");

  private static final org.apache.thrift.protocol.TField FIELD_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("fieldName", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField REVERSE_FIELD_DESC = new org.apache.thrift.protocol.TField("reverse", org.apache.thrift.protocol.TType.BOOL, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new SortFieldStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new SortFieldTupleSchemeFactory();

  public java.lang.String fieldName; // required
  public boolean reverse; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    FIELD_NAME((short)1, "fieldName"),
    REVERSE((short)2, "reverse");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // FIELD_NAME
          return FIELD_NAME;
        case 2: // REVERSE
          return REVERSE;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __REVERSE_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.FIELD_NAME, new org.apache.thrift.meta_data.FieldMetaData("fieldName", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.REVERSE, new org.apache.thrift.meta_data.FieldMetaData("reverse", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(SortField.class, metaDataMap);
  }

  public SortField() {
  }

  public SortField(
    java.lang.String fieldName,
    boolean reverse)
  {
    this();
    this.fieldName = fieldName;
    this.reverse = reverse;
    setReverseIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public SortField(SortField other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetFieldName()) {
      this.fieldName = other.fieldName;
    }
    this.reverse = other.reverse;
  }

  public SortField deepCopy() {
    return new SortField(this);
  }

  @Override
  public void clear() {
    this.fieldName = null;
    setReverseIsSet(false);
    this.reverse = false;
  }

  public java.lang.String getFieldName() {
    return this.fieldName;
  }

  public SortField setFieldName(java.lang.String fieldName) {
    this.fieldName = fieldName;
    return this;
  }

  public void unsetFieldName() {
    this.fieldName = null;
  }

  /** Returns true if field fieldName is set (has been assigned a value) and false otherwise */
  public boolean isSetFieldName() {
    return this.fieldName != null;
  }

  public void setFieldNameIsSet(boolean value) {
    if (!value) {
      this.fieldName = null;
    }
  }

  public boolean isReverse() {
    return this.reverse;
  }

  public SortField setReverse(boolean reverse) {
    this.reverse = reverse;
    setReverseIsSet(true);
    return this;
  }

  public void unsetReverse() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __REVERSE_ISSET_ID);
  }

  /** Returns true if field reverse is set (has been assigned a value) and false otherwise */
  public boolean isSetReverse() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __REVERSE_ISSET_ID);
  }

  public void setReverseIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __REVERSE_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case FIELD_NAME:
      if (value == null) {
        unsetFieldName();
      } else {
        setFieldName((java.lang.String)value);
      }
      break;

    case REVERSE:
      if (value == null) {
        unsetReverse();
      } else {
        setReverse((java.lang.Boolean)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case FIELD_NAME:
      return getFieldName();

    case REVERSE:
      return isReverse();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case FIELD_NAME:
      return isSetFieldName();
    case REVERSE:
      return isSetReverse();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof SortField)
      return this.equals((SortField)that);
    return false;
  }

  public boolean equals(SortField that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_fieldName = true && this.isSetFieldName();
    boolean that_present_fieldName = true && that.isSetFieldName();
    if (this_present_fieldName || that_present_fieldName) {
      if (!(this_present_fieldName && that_present_fieldName))
        return false;
      if (!this.fieldName.equals(that.fieldName))
        return false;
    }

    boolean this_present_reverse = true;
    boolean that_present_reverse = true;
    if (this_present_reverse || that_present_reverse) {
      if (!(this_present_reverse && that_present_reverse))
        return false;
      if (this.reverse != that.reverse)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetFieldName()) ? 131071 : 524287);
    if (isSetFieldName())
      hashCode = hashCode * 8191 + fieldName.hashCode();

    hashCode = hashCode * 8191 + ((reverse) ? 131071 : 524287);

    return hashCode;
  }

  @Override
  public int compareTo(SortField other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetFieldName()).compareTo(other.isSetFieldName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetFieldName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.fieldName, other.fieldName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetReverse()).compareTo(other.isSetReverse());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetReverse()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.reverse, other.reverse);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("SortField(");
    boolean first = true;

    sb.append("fieldName:");
    if (this.fieldName == null) {
      sb.append("null");
    } else {
      sb.append(this.fieldName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("reverse:");
    sb.append(this.reverse);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class SortFieldStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public SortFieldStandardScheme getScheme() {
      return new SortFieldStandardScheme();
    }
  }

  private static class SortFieldStandardScheme extends org.apache.thrift.scheme.StandardScheme<SortField> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, SortField struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // FIELD_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.fieldName = iprot.readString();
              struct.setFieldNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // REVERSE
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.reverse = iprot.readBool();
              struct.setReverseIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, SortField struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.fieldName != null) {
        oprot.writeFieldBegin(FIELD_NAME_FIELD_DESC);
        oprot.writeString(struct.fieldName);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(REVERSE_FIELD_DESC);
      oprot.writeBool(struct.reverse);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class SortFieldTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public SortFieldTupleScheme getScheme() {
      return new SortFieldTupleScheme();
    }
  }

  private static class SortFieldTupleScheme extends org.apache.thrift.scheme.TupleScheme<SortField> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, SortField struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetFieldName()) {
        optionals.set(0);
      }
      if (struct.isSetReverse()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetFieldName()) {
        oprot.writeString(struct.fieldName);
      }
      if (struct.isSetReverse()) {
        oprot.writeBool(struct.reverse);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, SortField struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.fieldName = iprot.readString();
        struct.setFieldNameIsSet(true);
      }
      if (incoming.get(1)) {
        struct.reverse = iprot.readBool();
        struct.setReverseIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

