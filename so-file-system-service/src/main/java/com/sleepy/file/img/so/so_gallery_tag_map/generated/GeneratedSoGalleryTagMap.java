package com.sleepy.file.img.so.so_gallery_tag_map.generated;

import com.sleepy.file.img.so.so_gallery_tag_map.SoGalleryTagMap;
import com.speedment.common.annotation.GeneratedCode;
import com.speedment.runtime.config.identifier.ColumnIdentifier;
import com.speedment.runtime.config.identifier.TableIdentifier;
import com.speedment.runtime.field.LongField;
import com.speedment.runtime.field.StringField;
import com.speedment.runtime.typemapper.TypeMapper;

/**
 * The generated base for the {@link
 * com.sleepy.file.img.so.so_gallery_tag_map.SoGalleryTagMap}-interface
 * representing entities of the {@code so_gallery_tag_map}-table in the
 * database.
 * <p>
 * This file has been automatically generated by Speedment. Any changes made to
 * it will be overwritten.
 *
 * @author Speedment
 */
@GeneratedCode("Speedment")
public interface GeneratedSoGalleryTagMap {

    /**
     * This Field corresponds to the {@link SoGalleryTagMap} field that can be
     * obtained using the {@link SoGalleryTagMap#getTagId()} method.
     */
    LongField<SoGalleryTagMap, Long> TAG_ID = LongField.create(
            Identifier.TAG_ID,
            SoGalleryTagMap::getTagId,
            SoGalleryTagMap::setTagId,
            TypeMapper.primitive(),
            false
    );
    /**
     * This Field corresponds to the {@link SoGalleryTagMap} field that can be
     * obtained using the {@link SoGalleryTagMap#getObjId()} method.
     */
    StringField<SoGalleryTagMap, String> OBJ_ID = StringField.create(
            Identifier.OBJ_ID,
            SoGalleryTagMap::getObjId,
            SoGalleryTagMap::setObjId,
            TypeMapper.identity(),
            false
    );

    /**
     * Returns the tagId of this SoGalleryTagMap. The tagId field corresponds to
     * the database column dev.dev.so_gallery_tag_map.tag_id.
     *
     * @return the tagId of this SoGalleryTagMap
     */
    long getTagId();

    /**
     * Returns the objId of this SoGalleryTagMap. The objId field corresponds to
     * the database column dev.dev.so_gallery_tag_map.obj_id.
     *
     * @return the objId of this SoGalleryTagMap
     */
    String getObjId();

    /**
     * Sets the tagId of this SoGalleryTagMap. The tagId field corresponds to
     * the database column dev.dev.so_gallery_tag_map.tag_id.
     *
     * @param tagId to set of this SoGalleryTagMap
     * @return this SoGalleryTagMap instance
     */
    SoGalleryTagMap setTagId(long tagId);

    /**
     * Sets the objId of this SoGalleryTagMap. The objId field corresponds to
     * the database column dev.dev.so_gallery_tag_map.obj_id.
     *
     * @param objId to set of this SoGalleryTagMap
     * @return this SoGalleryTagMap instance
     */
    SoGalleryTagMap setObjId(String objId);

    enum Identifier implements ColumnIdentifier<SoGalleryTagMap> {

        TAG_ID("tag_id"),
        OBJ_ID("obj_id");

        private final String columnId;
        private final TableIdentifier<SoGalleryTagMap> tableIdentifier;

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
            return "so_gallery_tag_map";
        }

        @Override
        public String getColumnId() {
            return this.columnId;
        }

        @Override
        public TableIdentifier<SoGalleryTagMap> asTableIdentifier() {
            return this.tableIdentifier;
        }
    }
}