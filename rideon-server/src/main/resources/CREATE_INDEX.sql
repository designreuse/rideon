CREATE INDEX idx_route_sidx ON routes USING GIST (geometry); 
CREATE INDEX idx_segment_sidx ON segments USING GIST (geometry); 