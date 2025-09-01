import React from "react";
import { motion } from "framer-motion";
import {
  Handshake,
  Building2,
  Ship,
  Truck,
  Box,
  Users,
  BarChart3,
  Sparkles,
  ArrowRight,
  Globe2,
  FileText,
  Settings,
  ShieldCheck,
  Mail,
} from "lucide-react";


const fade = {
  initial: { opacity: 0, y: 16 },
  animate: { opacity: 1, y: 0 },
  transition: { duration: 0.5, ease: "easeOut" },
};

export default function HomePage() {
  return (
    <div data-theme="business" className="min-h-screen bg-base-200 text-base-content">
      {/* Hero */}
      <section className="relative overflow-hidden">
        <div className="absolute inset-0 bg-gradient-to-br from-primary/10 via-transparent to-secondary/10" />
        <div className="container mx-auto px-4 py-12 md:py-16">
          <motion.div {...fade} className="grid md:grid-cols-2 gap-10 items-center">
            <div>
              <h1 className="text-4xl md:text-5xl font-extrabold leading-tight">
                Welcome to <span className="text-primary">CargoTrack</span>
              </h1>
              <p className="mt-4 text-base-content/70 max-w-2xl">
                Your central hub for container visibility. This is a static preview dashboard — plug in data later. Explore partners, quick actions, and upcoming features.
              </p>
              <div className="mt-6 flex flex-wrap gap-3">
                <button className="btn btn-primary gap-2">
                  <Sparkles className="size-5" /> Explore Features
                </button>
                <button className="btn btn-outline gap-2">
                  <FileText className="size-5" /> View Docs
                </button>
              </div>
              <div className="mt-6 flex items-center gap-6 text-sm text-base-content/60">
                <span className="inline-flex items-center gap-2"><Ship className="size-4" /> Ocean</span>
                <span className="inline-flex items-center gap-2"><Truck className="size-4" /> Inland</span>
                <span className="inline-flex items-center gap-2"><Globe2 className="size-4" /> Global
                  Network</span>
              </div>
            </div>
            <div>
              <div className="card bg-base-100 shadow-xl border border-base-300">
                <div className="card-body">
                  <h2 className="card-title">Quick Start</h2>
                  <div className="grid md:grid-cols-2 gap-3">
                    <div className="p-4 rounded-xl bg-base-200/60 border border-base-300">
                      <div className="font-medium mb-1">Create Container</div>
                      <p className="text-sm opacity-70">Add a new record to your system.</p>
                      <button className="btn btn-ghost btn-sm mt-3">Open</button>
                    </div>
                    <div className="p-4 rounded-xl bg-base-200/60 border border-base-300">
                      <div className="font-medium mb-1">Import List</div>
                      <p className="text-sm opacity-70">Upload CSV/Excel and validate.</p>
                      <button className="btn btn-ghost btn-sm mt-3">Open</button>
                    </div>
                    <div className="p-4 rounded-xl bg-base-200/60 border border-base-300">
                      <div className="font-medium mb-1">Generate Report</div>
                      <p className="text-sm opacity-70">Run static sample reports.</p>
                      <button className="btn btn-ghost btn-sm mt-3">Open</button>
                    </div>
                    <div className="p-4 rounded-xl bg-base-200/60 border border-base-300">
                      <div className="font-medium mb-1">Settings</div>
                      <p className="text-sm opacity-70">Configure basic preferences.</p>
                      <button className="btn btn-ghost btn-sm mt-3">Open</button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </motion.div>
        </div>
      </section>

      {/* Partners / Clients */}
      <section className="container mx-auto px-4 py-6">
        <div className="flex items-center justify-between mb-4">
          <h3 className="text-lg font-semibold">Our Business Partners & Clients</h3>
          <a className="link link-primary">See all</a>
        </div>
        <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-6 gap-4">
          {Array.from({ length: 12 }).map((_, i) => (
            <div key={i} className="group">
              <div className="aspect-[3/2] rounded-xl border border-base-300 bg-base-100 flex items-center justify-center shadow-sm transition group-hover:shadow-md">
                <Building2 className="size-7 opacity-60" />
              </div>
              <div className="mt-2 text-sm text-center opacity-70">Partner {i + 1}</div>
            </div>
          ))}
        </div>
      </section>

      {/* Placeholder Stats */}
      <section className="container mx-auto px-4 py-4">
        <div className="stats stats-vertical lg:stats-horizontal shadow w-full bg-base-100">
          <div className="stat">
            <div className="stat-figure text-primary"><Ship className="size-6" /></div>
            <div className="stat-title">Containers</div>
            <div className="stat-value text-primary">—</div>
            <div className="stat-desc">Static preview</div>
          </div>
          <div className="stat">
            <div className="stat-figure text-secondary"><Truck className="size-6" /></div>
            <div className="stat-title">Inland Moves</div>
            <div className="stat-value text-secondary">—</div>
            <div className="stat-desc">Static preview</div>
          </div>
          <div className="stat">
            <div className="stat-figure text-accent"><Users className="size-6" /></div>
            <div className="stat-title">Customers</div>
            <div className="stat-value text-accent">—</div>
            <div className="stat-desc">Static preview</div>
          </div>
          <div className="stat">
            <div className="stat-figure text-info"><BarChart3 className="size-6" /></div>
            <div className="stat-title">On-time Rate</div>
            <div className="stat-value text-info">—</div>
            <div className="stat-desc">Static preview</div>
          </div>
        </div>
      </section>

      {/* Features */}
      <section className="container mx-auto px-4 py-6">
        <div className="grid md:grid-cols-2 xl:grid-cols-3 gap-4">
          <div className="card bg-base-100 shadow border border-base-300">
            <div className="card-body">
              <div className="flex items-center gap-3">
                <Handshake className="size-5 text-primary" />
                <h4 className="card-title">Partner Network</h4>
              </div>
              <p className="text-sm opacity-70">Discover carriers, depots, and logistics partners you can connect with (static showcase).</p>
              <div className="card-actions justify-end"><button className="btn btn-ghost btn-sm">Learn more</button></div>
            </div>
          </div>
          <div className="card bg-base-100 shadow border border-base-300">
            <div className="card-body">
              <div className="flex items-center gap-3">
                <ShieldCheck className="size-5 text-success" />
                <h4 className="card-title">Compliance & Security</h4>
              </div>
              <p className="text-sm opacity-70">Static content to highlight standards, certifications, and safeguards.</p>
              <div className="card-actions justify-end"><button className="btn btn-ghost btn-sm">Learn more</button></div>
            </div>
          </div>
          <div className="card bg-base-100 shadow border border-base-300">
            <div className="card-body">
              <div className="flex items-center gap-3">
                <Settings className="size-5 text-info" />
                <h4 className="card-title">Configurations</h4>
              </div>
              <p className="text-sm opacity-70">Ports, locations, roles and SLAs. Showcase only—wire later.</p>
              <div className="card-actions justify-end"><button className="btn btn-ghost btn-sm">Learn more</button></div>
            </div>
          </div>
        </div>
      </section>

      {/* Latest Updates (Static Timeline) */}
      <section className="container mx-auto px-4 py-6">
        <div className="card bg-base-100 shadow border border-base-300">
          <div className="card-body">
            <h3 className="card-title">Latest Updates</h3>
            <ul className="timeline timeline-snap-icon max-md:timeline-compact timeline-vertical">
              <li>
                <div className="timeline-middle"><Sparkles className="size-4" /></div>
                <div className="timeline-start mb-2 md:text-end">
                  <time className="font-mono italic">Today</time>
                  <div className="text-lg font-semibold">Welcome Experience</div>
                  <p className="text-sm opacity-70">Static dashboard scaffolding added.</p>
                </div>
                <hr />
              </li>
              <li>
                <hr />
                <div className="timeline-middle"><Mail className="size-4" /></div>
                <div className="timeline-end mb-2">
                  <time className="font-mono italic">This Week</time>
                  <div className="text-lg font-semibold">Partner Invites</div>
                  <p className="text-sm opacity-70">Share invites with clients (placeholder).</p>
                </div>
                <hr />
              </li>
            </ul>
          </div>
        </div>
      </section>

      {/* Call to Action */}
      <section className="container mx-auto px-4 py-10">
        <div className="hero rounded-2xl bg-base-100 border border-base-300 shadow">
          <div className="hero-content text-center">
            <div className="max-w-2xl">
              <h2 className="text-3xl font-extrabold">Ready to build your logistics hub?</h2>
              <p className="py-4 text-base-content/70">
                This page is fully static. Hook up your APIs later for live stats, reports, and partner data.
              </p>
              <button className="btn btn-primary gap-2">
                Get Started <ArrowRight className="size-4" />
              </button>
            </div>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="footer footer-center p-6 bg-base-100 text-base-content border-t border-base-300">
        <aside>
          <p className="text-sm">© {new Date().getFullYear()} CargoTrack • Static Welcome Dashboard</p>
        </aside>
      </footer>
    </div>
  );
}
