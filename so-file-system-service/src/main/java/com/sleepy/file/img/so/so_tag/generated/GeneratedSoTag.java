package com.sleepy.file.img.so.so_tag.generated;

import com.sleepy.file.img.so.so_tag.SoTag;
import com.speedment.common.annotation.GeneratedCode;
import com.speedment.runtime.config.identifier.ColumnIdentifier;
import com.speedment.runtime.config.identifier.TableIdentifier;
import com.speedment.runtime.field.LongField;
import com.speedment.runtime.field.StringField;
import com.speedment.runtime.typemapper.TypeMapper;

/**
 * The generated base for the {@link
 * com.sleepy.file.img.so.so_tag.SoTag}-interface representing entities of the
 * {@code so_tag}-table in the database.
 * <p>
 * This file has been automatically generated by Speedment. Any changes made to
 * it will be overwritten.
 *
 * @author Speedment
 */
@GeneratedCode("Speedment")
public interface GeneratedSoTag {

    /**
     * This Field corresponds to the {@link SoTag} field that can be obtained
     * using the {@link SoTag#getId()} method.
     */
    LongField<SoTag, Long> ID = LongField.create(
            Identifier.ID,
            SoTag::getId,
            SoTag::setId,
            TypeMapper.primitive(),
            true
    );
    /**
     * This Field corresponds to the {@link SoTag} field that can be obtained
     * using the {@link SoTag#getTagName()} method.
     */
    StringField<SoTag, String> TAG_NAME = StringField.create(
            Identifier.TAG_NAME,
            SoTag::getTagName,
            SoTag::setTagName,
            TypeMapper.identity(),
            false
    );

    /**
     * Returns the id of this SoTag. The id field corresponds to the database
     * column dev.dev.so_tag.id.
     *
     * @return the id of this SoTag
     */
    long getId();

    /**
     * Returns the tagName of this SoTag. The tagName field corresponds to the
     * database column dev.dev.so_tag.tag_name.
     *
     * @return the tagName of this SoTag
     */
    String getTagName();

    /**
     * Sets the id of this SoTag. The id field corresponds to the database
     * column dev.dev.so_tag.id.
     *
     * @param id to set of this SoTag
     * @return this SoTag instance
     */
    SoTag setId(long id);

    /**
     * Sets the tagName of this SoTag. The tagName field corresponds to the
     * database column dev.dev.so_tag.tag_name.
     *
     * @param tagName to set of this SoTag
     * @return this SoTag instance
     */
    SoTag setTagName(String tagName);

    enum Identifier implements ColumnIdentifier<SoTag> {

        ID("id"),
        TAG_NAME("tag_name");

        private final String columnId;
        private final TableIdentifier<SoTag> tableIdentifier;

        Identifier(String columnId) {
            this.columnId = columnId;
            this.tableIdentifier = TableIdentifier.of(getDbmsId(),
                    getSchemaId(),
                    getTableId());
        }

        @Override
        public String getDbmsId() {
            return "dev";
        }

        @Override
        public String getSchemaId() {
            return "dev";
        }

        @Override
        public String getTableId() {
            return "so_tag";
        }

        @Override
        public String getColumnId() {
            return this.columnId;
        }

        @Override
        public TableIdentifier<SoTag> asTableIdentifier() {
            return this.tableIdentifier;
        }
    }
}