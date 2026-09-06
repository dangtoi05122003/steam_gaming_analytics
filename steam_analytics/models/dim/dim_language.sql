with base as (
    select distinct
        language
    from {{ ref('stg_steam')}}
)
select
    row_number() over(order by language) as language_id,
    language
from base