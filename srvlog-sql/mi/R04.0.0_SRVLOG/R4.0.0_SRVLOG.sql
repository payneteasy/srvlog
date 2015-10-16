set names utf8 collate utf8_general_ci;

drop table if exists unprocessed_snort_logs;

alter table snort_logs
    drop index idx_snort_logs_hash,
    drop column hash
;

alter table logs
    drop index idx_logs_hash,
    drop column has_snort_logs
;

alter table unprocessed_logs
    drop index idx_unprocessed_logs_hash,
    drop column has_snort_logs
;

commit;