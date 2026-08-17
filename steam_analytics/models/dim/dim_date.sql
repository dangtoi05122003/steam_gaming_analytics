select distinct
    cast(created_at as date) as date,
    extract(year from created_at) as year,
    extract(month from created_at) as month,
    extract(day from created_at) as day
from {{ref('stg_steam')}}
where extract(year from created_at) between 2010 and 2023