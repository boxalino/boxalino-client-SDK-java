/**
 * Autogenerated by Thrift Compiler (0.10.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.boxalino.p13n.api.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
/**
 * item found
 * 
 * <dl>
 * <dt>values</dt>
 * <dd>map containing name of the field and list of values as strings</dd>
 * <dd>if index contains no value for a field, empty array will be returned.</dd>
 * 
 * <dt>score</dt>
 * <dd>index score of the hit</dd>
 * 
 * <dt>scenarioId</dt>
 * <dd>source scenarioId in case of mixed recommendations modes</dd>
 * </dl>
 */
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.10.0)", date = "2017-01-25")
public class Hit implements org.apache.thrift.TBase<Hit, Hit._Fields>, java.io.Serializable, Cloneable, Comparable<Hit> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Hit");

  private static final org.apache.thrift.protocol.TField VALUES_FIELD_DESC = new org.apache.thrift.protocol.TField("values", org.apache.thrift.protocol.TType.MAP, (short)1);
  private static final org.apache.thrift.protocol.TField SCORE_FIELD_DESC = new org.apache.thrift.protocol.TField("score", org.apache.thrift.protocol.TType.DOUBLE, (short)2);
  private static final org.apache.thrift.protocol.TField SCENARIO_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("scenarioId", org.apache.thrift.protocol.TType.STRING, (short)30);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new HitStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new HitTupleSchemeFactory();

  public java.util.Map<java.lang.String,java.util.List<java.lang.String>> values; // required
  public double score; // required
  public java.lang.String scenarioId; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    VALUES((short)1, "values"),
    SCORE((short)2, "score"),
    SCENARIO_ID((short)30, "scenarioId");

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
        case 1: // VALUES
          return VALUES;
        case 2: // SCORE
          return SCORE;
        case 30: // SCENARIO_ID
          return SCENARIO_ID;
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
  private static final int __SCORE_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.VALUES, new org.apache.thrift.meta_data.FieldMetaData("values", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING), 
            new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
                new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)))));
    tmpMap.put(_Fields.SCORE, new org.apache.thrift.meta_data.FieldMetaData("score", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.DOUBLE)));
    tmpMap.put(_Fields.SCENARIO_ID, new org.apache.thrift.meta_data.FieldMetaData("scenarioId", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Hit.class, metaDataMap);
  }

  public Hit() {
  }

  public Hit(
    java.util.Map<java.lang.String,java.util.List<java.lang.String>> values,
    double score,
    java.lang.String scenarioId)
  {
    this();
    this.values = values;
    this.score = score;
    setScoreIsSet(true);
    this.scenarioId = scenarioId;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Hit(Hit other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetValues()) {
      java.util.Map<java.lang.String,java.util.List<java.lang.String>> __this__values = new java.util.HashMap<java.lang.String,java.util.List<java.lang.String>>(other.values.size());
      for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.String>> other_element : other.values.entrySet()) {

        java.lang.String other_element_key = other_element.getKey();
        java.util.List<java.lang.String> other_element_value = other_element.getValue();

        java.lang.String __this__values_copy_key = other_element_key;

        java.util.List<java.lang.String> __this__values_copy_value = new java.util.ArrayList<java.lang.String>(other_element_value);

        __this__values.put(__this__values_copy_key, __this__values_copy_value);
      }
      this.values = __this__values;
    }
    this.score = other.score;
    if (other.isSetScenarioId()) {
      this.scenarioId = other.scenarioId;
    }
  }

  public Hit deepCopy() {
    return new Hit(this);
  }

  @Override
  public void clear() {
    this.values = null;
    setScoreIsSet(false);
    this.score = 0.0;
    this.scenarioId = null;
  }

  public int getValuesSize() {
    return (this.values == null) ? 0 : this.values.size();
  }

  public void putToValues(java.lang.String key, java.util.List<java.lang.String> val) {
    if (this.values == null) {
      this.values = new java.util.HashMap<java.lang.String,java.util.List<java.lang.String>>();
    }
    this.values.put(key, val);
  }

  public java.util.Map<java.lang.String,java.util.List<java.lang.String>> getValues() {
    return this.values;
  }

  public Hit setValues(java.util.Map<java.lang.String,java.util.List<java.lang.String>> values) {
    this.values = values;
    return this;
  }

  public void unsetValues() {
    this.values = null;
  }

  /** Returns true if field values is set (has been assigned a value) and false otherwise */
  public boolean isSetValues() {
    return this.values != null;
  }

  public void setValuesIsSet(boolean value) {
    if (!value) {
      this.values = null;
    }
  }

  public double getScore() {
    return this.score;
  }

  public Hit setScore(double score) {
    this.score = score;
    setScoreIsSet(true);
    return this;
  }

  public void unsetScore() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __SCORE_ISSET_ID);
  }

  /** Returns true if field score is set (has been assigned a value) and false otherwise */
  public boolean isSetScore() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __SCORE_ISSET_ID);
  }

  public void setScoreIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __SCORE_ISSET_ID, value);
  }

  public java.lang.String getScenarioId() {
    return this.scenarioId;
  }

  public Hit setScenarioId(java.lang.String scenarioId) {
    this.scenarioId = scenarioId;
    return this;
  }

  public void unsetScenarioId() {
    this.scenarioId = null;
  }

  /** Returns true if field scenarioId is set (has been assigned a value) and false otherwise */
  public boolean isSetScenarioId() {
    return this.scenarioId != null;
  }

  public void setScenarioIdIsSet(boolean value) {
    if (!value) {
      this.scenarioId = null;
    }
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case VALUES:
      if (value == null) {
        unsetValues();
      } else {
        setValues((java.util.Map<java.lang.String,java.util.List<java.lang.String>>)value);
      }
      break;

    case SCORE:
      if (value == null) {
        unsetScore();
      } else {
        setScore((java.lang.Double)value);
      }
      break;

    case SCENARIO_ID:
      if (value == null) {
        unsetScenarioId();
      } else {
        setScenarioId((java.lang.String)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case VALUES:
      return getValues();

    case SCORE:
      return getScore();

    case SCENARIO_ID:
      return getScenarioId();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case VALUES:
      return isSetValues();
    case SCORE:
      return isSetScore();
    case SCENARIO_ID:
      return isSetScenarioId();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof Hit)
      return this.equals((Hit)that);
    return false;
  }

  public boolean equals(Hit that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_values = true && this.isSetValues();
    boolean that_present_values = true && that.isSetValues();
    if (this_present_values || that_present_values) {
      if (!(this_present_values && that_present_values))
        return false;
      if (!this.values.equals(that.values))
        return false;
    }

    boolean this_present_score = true;
    boolean that_present_score = true;
    if (this_present_score || that_present_score) {
      if (!(this_present_score && that_present_score))
        return false;
      if (this.score != that.score)
        return false;
    }

    boolean this_present_scenarioId = true && this.isSetScenarioId();
    boolean that_present_scenarioId = true && that.isSetScenarioId();
    if (this_present_scenarioId || that_present_scenarioId) {
      if (!(this_present_scenarioId && that_present_scenarioId))
        return false;
      if (!this.scenarioId.equals(that.scenarioId))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetValues()) ? 131071 : 524287);
    if (isSetValues())
      hashCode = hashCode * 8191 + values.hashCode();

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(score);

    hashCode = hashCode * 8191 + ((isSetScenarioId()) ? 131071 : 524287);
    if (isSetScenarioId())
      hashCode = hashCode * 8191 + scenarioId.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(Hit other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetValues()).compareTo(other.isSetValues());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetValues()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.values, other.values);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetScore()).compareTo(other.isSetScore());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetScore()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.score, other.score);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetScenarioId()).compareTo(other.isSetScenarioId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetScenarioId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.scenarioId, other.scenarioId);
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
    java.lang.StringBuilder sb = new java.lang.StringBuilder("Hit(");
    boolean first = true;

    sb.append("values:");
    if (this.values == null) {
      sb.append("null");
    } else {
      sb.append(this.values);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("score:");
    sb.append(this.score);
    first = false;
    if (!first) sb.append(", ");
    sb.append("scenarioId:");
    if (this.scenarioId == null) {
      sb.append("null");
    } else {
      sb.append(this.scenarioId);
    }
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

  private static class HitStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public HitStandardScheme getScheme() {
      return new HitStandardScheme();
    }
  }

  private static class HitStandardScheme extends org.apache.thrift.scheme.StandardScheme<Hit> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Hit struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // VALUES
            if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
              {
                org.apache.thrift.protocol.TMap _map142 = iprot.readMapBegin();
                struct.values = new java.util.HashMap<java.lang.String,java.util.List<java.lang.String>>(2*_map142.size);
                java.lang.String _key143;
                java.util.List<java.lang.String> _val144;
                for (int _i145 = 0; _i145 < _map142.size; ++_i145)
                {
                  _key143 = iprot.readString();
                  {
                    org.apache.thrift.protocol.TList _list146 = iprot.readListBegin();
                    _val144 = new java.util.ArrayList<java.lang.String>(_list146.size);
                    java.lang.String _elem147;
                    for (int _i148 = 0; _i148 < _list146.size; ++_i148)
                    {
                      _elem147 = iprot.readString();
                      _val144.add(_elem147);
                    }
                    iprot.readListEnd();
                  }
                  struct.values.put(_key143, _val144);
                }
                iprot.readMapEnd();
              }
              struct.setValuesIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // SCORE
            if (schemeField.type == org.apache.thrift.protocol.TType.DOUBLE) {
              struct.score = iprot.readDouble();
              struct.setScoreIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 30: // SCENARIO_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.scenarioId = iprot.readString();
              struct.setScenarioIdIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, Hit struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.values != null) {
        oprot.writeFieldBegin(VALUES_FIELD_DESC);
        {
          oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.LIST, struct.values.size()));
          for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.String>> _iter149 : struct.values.entrySet())
          {
            oprot.writeString(_iter149.getKey());
            {
              oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, _iter149.getValue().size()));
              for (java.lang.String _iter150 : _iter149.getValue())
              {
                oprot.writeString(_iter150);
              }
              oprot.writeListEnd();
            }
          }
          oprot.writeMapEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(SCORE_FIELD_DESC);
      oprot.writeDouble(struct.score);
      oprot.writeFieldEnd();
      if (struct.scenarioId != null) {
        oprot.writeFieldBegin(SCENARIO_ID_FIELD_DESC);
        oprot.writeString(struct.scenarioId);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class HitTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public HitTupleScheme getScheme() {
      return new HitTupleScheme();
    }
  }

  private static class HitTupleScheme extends org.apache.thrift.scheme.TupleScheme<Hit> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Hit struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetValues()) {
        optionals.set(0);
      }
      if (struct.isSetScore()) {
        optionals.set(1);
      }
      if (struct.isSetScenarioId()) {
        optionals.set(2);
      }
      oprot.writeBitSet(optionals, 3);
      if (struct.isSetValues()) {
        {
          oprot.writeI32(struct.values.size());
          for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.String>> _iter151 : struct.values.entrySet())
          {
            oprot.writeString(_iter151.getKey());
            {
              oprot.writeI32(_iter151.getValue().size());
              for (java.lang.String _iter152 : _iter151.getValue())
              {
                oprot.writeString(_iter152);
              }
            }
          }
        }
      }
      if (struct.isSetScore()) {
        oprot.writeDouble(struct.score);
      }
      if (struct.isSetScenarioId()) {
        oprot.writeString(struct.scenarioId);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Hit struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(3);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TMap _map153 = new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.LIST, iprot.readI32());
          struct.values = new java.util.HashMap<java.lang.String,java.util.List<java.lang.String>>(2*_map153.size);
          java.lang.String _key154;
          java.util.List<java.lang.String> _val155;
          for (int _i156 = 0; _i156 < _map153.size; ++_i156)
          {
            _key154 = iprot.readString();
            {
              org.apache.thrift.protocol.TList _list157 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRING, iprot.readI32());
              _val155 = new java.util.ArrayList<java.lang.String>(_list157.size);
              java.lang.String _elem158;
              for (int _i159 = 0; _i159 < _list157.size; ++_i159)
              {
                _elem158 = iprot.readString();
                _val155.add(_elem158);
              }
            }
            struct.values.put(_key154, _val155);
          }
        }
        struct.setValuesIsSet(true);
      }
      if (incoming.get(1)) {
        struct.score = iprot.readDouble();
        struct.setScoreIsSet(true);
      }
      if (incoming.get(2)) {
        struct.scenarioId = iprot.readString();
        struct.setScenarioIdIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

