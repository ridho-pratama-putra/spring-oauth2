--liquibase formatted sql
--changeset zulvan:22.2.03

INSERT INTO role_permission(role_id, permission_id)
VALUES((select id from app_role where code = 'MAXI_ACM'), (select id from app_permission where code = 'APT_VIEW'));
INSERT INTO role_permission(role_id, permission_id)
VALUES((select id from app_role where code = 'MAXI_ACM'), (select id from app_permission where code = 'APT_MODIFY'));
INSERT INTO role_permission(role_id, permission_id)
VALUES((select id from app_role where code = 'MAXI_ACM'), (select id from app_permission where code = 'HST_VIEW'));
INSERT INTO role_permission(role_id, permission_id)
VALUES((select id from app_role where code = 'MAXI_ACM'), (select id from app_permission where code = 'VIDCAL'));
INSERT INTO role_permission(role_id, permission_id)
VALUES((select id from app_role where code = 'MAXI_SPV'), (select id from app_permission where code = 'APT_VIEW_CHANNEL'));
INSERT INTO role_permission(role_id, permission_id)
VALUES((select id from app_role where code = 'MAXI_SPV'), (select id from app_permission where code = 'HST_VIEW_CHANNEL'));
INSERT INTO role_permission(role_id, permission_id)
VALUES((select id from app_role where code = 'MAXI_SPV'), (select id from app_permission where code = 'ADT_VIEW'));
